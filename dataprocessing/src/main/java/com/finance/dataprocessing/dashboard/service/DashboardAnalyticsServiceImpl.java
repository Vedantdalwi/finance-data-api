package com.finance.dataprocessing.dashboard.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finance.dataprocessing.dashboard.dto.CategorySummaryDto;
import com.finance.dataprocessing.dashboard.dto.DashboardSummaryDto;
import com.finance.dataprocessing.dashboard.dto.MonthlyTrendDto;
import com.finance.dataprocessing.dashboard.dto.MonthlyTrendProjection;
import com.finance.dataprocessing.dashboard.dto.TopSpenderProjection;
import com.finance.dataprocessing.dashboard.dto.UserCategoryBreakdownProjection;
import com.finance.dataprocessing.record.enums.TransactionType;
import com.finance.dataprocessing.record.repository.FinancialRecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardAnalyticsServiceImpl implements DashboardAnalyticsService {

	private final FinancialRecordRepository financialRecordRepository;

    @Override
    public DashboardSummaryDto getFinanceSummary() {

        BigDecimal income = financialRecordRepository.getTotalIncome();
        BigDecimal expense = financialRecordRepository.getTotalExpense();

        return new DashboardSummaryDto(
                income,
                expense,
                income.subtract(expense)
        );
    }


    @Override
    public List<CategorySummaryDto> getFinanceCategorySummary() {

        return financialRecordRepository.getCategorySummary()
                .stream()
                .map(p -> new CategorySummaryDto(
                        p.getCategory(),
                        p.getTotalAmount()
                ))
                .toList();
    }


    @Override
    public List<MonthlyTrendDto> getMonthlyTrends() {

        List<MonthlyTrendProjection> results = financialRecordRepository.getMonthlyTrends();

        Map<String, MonthlyTrendDto> map = new LinkedHashMap<>();

        for (MonthlyTrendProjection row : results) {

            map.putIfAbsent(
                    row.getMonth(),
                    new MonthlyTrendDto(row.getMonth(), BigDecimal.ZERO, BigDecimal.ZERO)
            );

            MonthlyTrendDto dto = map.get(row.getMonth());

            if (row.getType() == TransactionType.INCOME) {
                dto.setIncome(row.getTotalAmount());
            } else {
                dto.setExpense(row.getTotalAmount());
            }
        }

        return new ArrayList<>(map.values());
    }


    @Override
    public DashboardSummaryDto getUserSummary(UUID userId) {

        BigDecimal income = financialRecordRepository.getTotalIncomeByUser(userId);
        BigDecimal expense = financialRecordRepository.getTotalExpenseByUser(userId);

        return new DashboardSummaryDto(
                income,
                expense,
                income.subtract(expense)
        );
    }

    
    @Override
    public List<UserCategoryBreakdownProjection> getCategoryBreakdownPerUser(UUID userId) {
        return financialRecordRepository.getCategoryBreakdownPerUser(userId);
    }

    @Override
    public List<TopSpenderProjection> getTopSpenders(int limit) {
        return financialRecordRepository.getTopSpenders(limit);
    }

}
