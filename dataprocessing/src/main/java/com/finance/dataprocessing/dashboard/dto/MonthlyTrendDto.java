package com.finance.dataprocessing.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class MonthlyTrendDto {

    private String month; 

    private BigDecimal income;

    private BigDecimal expense;
}
