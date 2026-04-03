package com.finance.dataprocessing.record.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.finance.dataprocessing.common.response.ApiResponse;
import com.finance.dataprocessing.record.dto.FinancialRecordRequestDto;
import com.finance.dataprocessing.record.dto.FinancialRecordResponseDto;
import com.finance.dataprocessing.record.dto.FinancialRecordUpdateDto;
import com.finance.dataprocessing.record.enums.TransactionType;
import com.finance.dataprocessing.record.service.FinancialRecordService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/finance/records")
@RequiredArgsConstructor
public class FinancialRecordController {

    private final FinancialRecordService financialRecordService;

 
    @PostMapping
    public ResponseEntity<ApiResponse<FinancialRecordResponseDto>> createRecord(
            @Valid @RequestBody FinancialRecordRequestDto request,
            @RequestParam UUID userId   // Todo: later to be replaced
    ) {

        FinancialRecordResponseDto response =
                financialRecordService.createRecord(request, userId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Record created successfully", response, Instant.now())
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FinancialRecordResponseDto>> getRecordById(
            @PathVariable UUID id
    ) {

        FinancialRecordResponseDto response =
                financialRecordService.getRecordById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Record fetched successfully", response, Instant.now())
        );
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<FinancialRecordResponseDto>>> getRecords(

            @RequestParam UUID userId,

            @RequestParam(required = false) Instant startDate,
            @RequestParam(required = false) Instant endDate,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") Integer pageNo
    ) {

        List<FinancialRecordResponseDto> records =
                financialRecordService.getRecords(
                        userId, startDate, endDate, type, category, search, pageNo
                );

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Records fetched successfully", records, Instant.now())
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FinancialRecordResponseDto>> updateRecord(
            @PathVariable UUID id,
            @RequestBody FinancialRecordUpdateDto request
    ) {

        FinancialRecordResponseDto response =
                financialRecordService.updateRecord(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Record updated successfully", response, Instant.now())
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRecord(
            @PathVariable UUID id
    ) {

        financialRecordService.deleteRecord(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Record deleted successfully", null, Instant.now())
        );
    }
}
