package com.finance.dataprocessing.dashboard.dto;

import java.math.BigDecimal;

public interface UserCategoryBreakdownProjection {
    String getUserName();
    String getCategory();
    String getType();
    BigDecimal getTotalAmount();
    Long getTransactionCount();
}
