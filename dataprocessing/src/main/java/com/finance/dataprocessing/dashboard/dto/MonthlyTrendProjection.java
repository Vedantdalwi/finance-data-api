package com.finance.dataprocessing.dashboard.dto;

import java.math.BigDecimal;

import com.finance.dataprocessing.record.enums.TransactionType;

public interface MonthlyTrendProjection {

    String getMonth();

    TransactionType getType();

    BigDecimal getTotalAmount();
}
