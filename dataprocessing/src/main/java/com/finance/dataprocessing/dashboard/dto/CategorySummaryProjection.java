package com.finance.dataprocessing.dashboard.dto;

import java.math.BigDecimal;

public interface CategorySummaryProjection {

    String getCategory();

    BigDecimal getTotalAmount();
}