package com.finance.dataprocessing.record.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.finance.dataprocessing.record.dto.FinancialRecordRequestDto;
import com.finance.dataprocessing.record.dto.FinancialRecordResponseDto;
import com.finance.dataprocessing.record.dto.FinancialRecordUpdateDto;
import com.finance.dataprocessing.record.enums.TransactionType;

public interface FinancialRecordService {

    FinancialRecordResponseDto createRecord(FinancialRecordRequestDto request, UUID userId);

    FinancialRecordResponseDto getRecordById(UUID id);

    FinancialRecordResponseDto updateRecord(UUID id, FinancialRecordUpdateDto request);

    void deleteRecord(UUID id);

    List<FinancialRecordResponseDto> getRecords(
            UUID userId,
            Instant startDate,
            Instant endDate,
            TransactionType type,
            String category,
            String search,
            Integer pageNo
    );
}
