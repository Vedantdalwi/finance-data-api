package com.finance.dataprocessing.record.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finance.dataprocessing.common.exception.NotFoundException;
import com.finance.dataprocessing.record.dto.FinancialRecordRequestDto;
import com.finance.dataprocessing.record.dto.FinancialRecordResponseDto;
import com.finance.dataprocessing.record.dto.FinancialRecordUpdateDto;
import com.finance.dataprocessing.record.entity.FinancialRecord;
import com.finance.dataprocessing.record.enums.TransactionType;
import com.finance.dataprocessing.record.repository.FinancialRecordRepository;
import com.finance.dataprocessing.user.entity.User;
import com.finance.dataprocessing.user.repository.UserRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

@Service
@RequiredArgsConstructor
@Transactional
public class FinancialRecordServiceImpl implements FinancialRecordService {

    private final FinancialRecordRepository financialRecordRepository;
    private final UserRepository userRepository;

    @Override
    public FinancialRecordResponseDto createRecord(FinancialRecordRequestDto request, UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        FinancialRecord record = new FinancialRecord();

        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(normalizeCategory(request.getCategory()));
        record.setDescription(request.getDescription());

        record.setTransactionDate(
                request.getTransactionDate() != null
                        ? request.getTransactionDate()
                        : Instant.now()
        );

        record.setCreatedBy(user);
        record.setIsDeleted(false);

        FinancialRecord saved = financialRecordRepository.saveAndFlush(record);

        return mapToDto(saved);
    }

    
    @Override
    @Transactional(readOnly = true)
    public FinancialRecordResponseDto getRecordById(UUID id) {

        FinancialRecord record = financialRecordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Record not found"));

        if (Boolean.TRUE.equals(record.getIsDeleted())) {
            throw new RuntimeException("This record has been deleted");
        }

        return mapToDto(record);
    }


    @Override
    public FinancialRecordResponseDto updateRecord(UUID id, FinancialRecordUpdateDto request) {

        FinancialRecord record = financialRecordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Record not found"));

        if (Boolean.TRUE.equals(record.getIsDeleted())) {
            throw new RuntimeException("Record not found");
        }

        if (request.getAmount() != null) {
            record.setAmount(request.getAmount());
        }

        if (request.getType() != null) {
            record.setType(request.getType());
        }

        if (request.getCategory() != null) {
            record.setCategory(normalizeCategory(request.getCategory()));
        }

        if (request.getDescription() != null) {
            record.setDescription(request.getDescription());
        }

        if (request.getTransactionDate() != null) {
            record.setTransactionDate(request.getTransactionDate());
        }

        FinancialRecord updated = financialRecordRepository.save(record);

        return mapToDto(updated);
    }


    @Override
    public void deleteRecord(UUID id) {

        FinancialRecord record = financialRecordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Record not found"));

        if (Boolean.TRUE.equals(record.getIsDeleted())) {
            throw new RuntimeException("Record already deleted");
        }

        record.setIsDeleted(true);

        financialRecordRepository.save(record);
    }


    @Override
    @Transactional(readOnly = true)
    public List<FinancialRecordResponseDto> getRecords(
            UUID userId,
            Instant startDate,
            Instant endDate,
            TransactionType type,
            String category,
            String search,
            Integer pageNo
    ) {
    	
    	PageRequest pageRequest = PageRequest.of(pageNo, 10, Sort.by(Sort.Direction.DESC, "transactionDate")); //pagesize default to 10

        Specification<FinancialRecord> spec = (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    cb.equal(root.get("createdBy").get("id"), userId)
            );

            predicates.add(
                    cb.isFalse(root.get("isDeleted"))
            );

            if (startDate != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(root.get("transactionDate"), startDate)
                );
            }

            if (endDate != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(root.get("transactionDate"), endDate)
                );
            }

            if (type != null) {
                predicates.add(
                        cb.equal(root.get("type"), type)
                );
            }

            if (category != null && !category.isBlank()) {
                predicates.add(
                        cb.equal(
                                cb.lower(root.get("category")),
                                category.trim().toLowerCase()
                        )
                );
            }
                      
            //search 
            if (search != null && !search.isBlank()) {

                String pattern = "%" + search.trim().toLowerCase() + "%";

                Join<Object, Object> userJoin = root.join("createdBy");

                Predicate categoryMatch = cb.like(
                        cb.lower(root.get("category")), pattern
                );

                Predicate descriptionMatch = cb.like(
                        cb.lower(root.get("description")), pattern
                );

                Predicate userNameMatch = cb.like(
                        cb.lower(userJoin.get("userName")), pattern
                );

                Predicate fullNameMatch = cb.like(
                        cb.lower(userJoin.get("fullName")), pattern
                );

                Predicate emailMatch = cb.like(
                        cb.lower(userJoin.get("email")), pattern
                );

                predicates.add(
                        cb.or(
                                categoryMatch,
                                descriptionMatch,
                                userNameMatch,
                                fullNameMatch,
                                emailMatch
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<FinancialRecord> records = financialRecordRepository.findAll(spec, pageRequest);

        return records.stream()
                .map(this::mapToDto)
                .toList();
    }

    
    //helper

    private String normalizeCategory(String category) {
        return category == null ? null : category.trim().toLowerCase();
    }

    private FinancialRecordResponseDto mapToDto(FinancialRecord record) {

        FinancialRecordResponseDto dto = new FinancialRecordResponseDto();

        dto.setId(record.getId());
        dto.setAmount(record.getAmount());
        dto.setType(record.getType());
        dto.setCategory(record.getCategory());
        dto.setDescription(record.getDescription());
        dto.setTransactionDate(record.getTransactionDate());
        dto.setCreatedBy(record.getCreatedBy().getId());
        dto.setCreatedAt(record.getCreatedAt());
        dto.setUpdatedAt(record.getUpdatedAt());

        return dto;
    }


}