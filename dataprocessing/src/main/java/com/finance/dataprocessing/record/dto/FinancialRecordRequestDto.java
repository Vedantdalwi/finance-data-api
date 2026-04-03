package com.finance.dataprocessing.record.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

import com.finance.dataprocessing.record.enums.TransactionType;

@Getter
@Setter
public class FinancialRecordRequestDto {

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private TransactionType type;

    @NotNull
    private String category;

    private String description;

    private Instant transactionDate;
}
