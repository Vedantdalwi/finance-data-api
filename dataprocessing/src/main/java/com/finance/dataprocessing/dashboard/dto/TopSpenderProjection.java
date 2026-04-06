package com.finance.dataprocessing.dashboard.dto;

import java.math.BigDecimal;

public interface TopSpenderProjection {
    String getUserName();
    String getFullName();
    BigDecimal getTotalExpense();
    Long getTransactionCount();
    BigDecimal getAverageTransactionAmount();
}
