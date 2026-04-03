package com.finance.dataprocessing.record.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.finance.dataprocessing.record.enums.TransactionType;

@Getter
@Setter
public class FinancialRecordResponseDto {

    private UUID id;

    private BigDecimal amount;

    private TransactionType type;

    private String category;

    private String description;

    private Instant transactionDate;

    private UUID createdBy;

    private Instant createdAt;

    private Instant updatedAt;
}
