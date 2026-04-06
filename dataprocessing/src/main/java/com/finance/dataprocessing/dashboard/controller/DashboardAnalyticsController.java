package com.finance.dataprocessing.dashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.finance.dataprocessing.common.response.ApiResponse;
import com.finance.dataprocessing.dashboard.dto.*;
import com.finance.dataprocessing.dashboard.service.DashboardAnalyticsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;


import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Tag(name = "Dashboard Analytics", description = "Analytics and financial insights APIs")
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardAnalyticsController {

    private final DashboardAnalyticsService dashboardAnalyticsService;

   
    @Operation(
            summary = "Get Overall Financial Summary",
            description = "Returns total income, total expense, and net balance across all users"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Summary fetched successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DashboardSummaryDto.class)
                    )
            )
    })
    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<ApiResponse<DashboardSummaryDto>> getOverallSummary() {

        DashboardSummaryDto response = dashboardAnalyticsService.getFinanceSummary();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Summary fetched successfully", response, Instant.now())
        );
    }


    @Operation(
            summary = "Get Category Summary",
            description = "Returns aggregated totals grouped by transaction categories"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Category summary fetched successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = CategorySummaryDto.class)
                            )
                    )
            )
    })
    @GetMapping("/categories")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<ApiResponse<List<CategorySummaryDto>>> getCategorySummary() {

        List<CategorySummaryDto> response =
                dashboardAnalyticsService.getFinanceCategorySummary();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Category summary fetched successfully", response, Instant.now())
        );
    }

    @Operation(
            summary = "Get Monthly Trends",
            description = "Returns month-wise income and expense trends for analytics visualization"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Monthly trends fetched successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = MonthlyTrendDto.class)
                            )
                    )
            )
    })
    @GetMapping("/trends")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<ApiResponse<List<MonthlyTrendDto>>> getMonthlyTrends() {

        List<MonthlyTrendDto> response =
                dashboardAnalyticsService.getMonthlyTrends();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Monthly trends fetched successfully", response, Instant.now())
        );
    }

    @Operation(
            summary = "Get User Financial Summary",
            description = "Returns total income, expense, and balance for a specific user"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User summary fetched successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DashboardSummaryDto.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/users/{userId}/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<ApiResponse<DashboardSummaryDto>> getUserSummary(
            @PathVariable UUID userId
    ) {

        DashboardSummaryDto response =
                dashboardAnalyticsService.getUserSummary(userId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "User summary fetched successfully", response, Instant.now())
        );
    }
    
    @Operation(
            summary = "Get Category Breakdown Per User",
            description = "Returns category wise income and expense breakdown. Pass userId to filter by specific user, leave empty for all users"
    )
    @GetMapping("/category")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<ApiResponse<List<UserCategoryBreakdownProjection>>> getCategoryBreakdown(
            @RequestParam(required = false) UUID userId
    ) {
        List<UserCategoryBreakdownProjection> response =
                dashboardAnalyticsService.getCategoryBreakdownPerUser(userId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Category breakdown fetched successfully", response, Instant.now())
        );
    }

    @Operation(
            summary = "Get Top Spenders",
            description = "Returns top users ranked by total expenses with average transaction amount"
    )
    @GetMapping("/top-spenders")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<ApiResponse<List<TopSpenderProjection>>> getTopSpenders(
            @RequestParam(defaultValue = "5") int limit
    ) {
        List<TopSpenderProjection> response =
                dashboardAnalyticsService.getTopSpenders(limit);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Top spenders fetched successfully", response, Instant.now())
        );
    }
}