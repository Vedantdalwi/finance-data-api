package com.finance.dataprocessing.dashboard.service;

import java.util.List;
import java.util.UUID;

import com.finance.dataprocessing.dashboard.dto.CategorySummaryDto;
import com.finance.dataprocessing.dashboard.dto.DashboardSummaryDto;
import com.finance.dataprocessing.dashboard.dto.MonthlyTrendDto;
import com.finance.dataprocessing.dashboard.dto.TopSpenderProjection;
import com.finance.dataprocessing.dashboard.dto.UserCategoryBreakdownProjection;

public interface DashboardAnalyticsService {

    DashboardSummaryDto getFinanceSummary();

    List<CategorySummaryDto> getFinanceCategorySummary();

    List<MonthlyTrendDto> getMonthlyTrends();

    DashboardSummaryDto getUserSummary(UUID userId);
    
    List<UserCategoryBreakdownProjection> getCategoryBreakdownPerUser(UUID userId);
    
    List<TopSpenderProjection> getTopSpenders(int limit);
}
