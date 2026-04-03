package com.finance.dataprocessing.record.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

import com.finance.dataprocessing.record.enums.TransactionType;

@Getter
@Setter
public class FinancialRecordUpdateDto {

    private BigDecimal amount;

    private TransactionType type;

    private String category;

    private String description;

    private Instant transactionDate;
}
