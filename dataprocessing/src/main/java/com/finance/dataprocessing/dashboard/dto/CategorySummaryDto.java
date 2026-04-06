package com.finance.dataprocessing.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class CategorySummaryDto {

    private String category;
    private BigDecimal totalAmount;
}
