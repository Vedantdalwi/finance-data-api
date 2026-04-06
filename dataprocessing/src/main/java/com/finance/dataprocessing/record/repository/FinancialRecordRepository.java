package com.finance.dataprocessing.record.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finance.dataprocessing.dashboard.dto.CategorySummaryProjection;
import com.finance.dataprocessing.dashboard.dto.MonthlyTrendProjection;
import com.finance.dataprocessing.dashboard.dto.TopSpenderProjection;
import com.finance.dataprocessing.dashboard.dto.UserCategoryBreakdownProjection;
import com.finance.dataprocessing.record.entity.FinancialRecord;

public interface FinancialRecordRepository extends JpaRepository< FinancialRecord, UUID>, JpaSpecificationExecutor<FinancialRecord> {

	
	//Analytics Queries
	
	@Query("""
		    SELECT COALESCE(SUM(fr.amount), 0)
		    FROM FinancialRecord fr
		    WHERE fr.type = 'INCOME'
		      AND fr.isDeleted = false
		""")
		BigDecimal getTotalIncome();
	
	
	@Query("""
		    SELECT COALESCE(SUM(fr.amount), 0)
		    FROM FinancialRecord fr
		    WHERE fr.type = 'EXPENSE'
		      AND fr.isDeleted = false
		""")
		BigDecimal getTotalExpense();
	
	@Query("""
		    SELECT fr.category AS category, SUM(fr.amount) AS totalAmount
		    FROM FinancialRecord fr
		    WHERE fr.isDeleted = false
		    GROUP BY fr.category
		""")
		List<CategorySummaryProjection> getCategorySummary();
	
	@Query("""
		    SELECT 
		        FUNCTION('DATE_FORMAT', fr.transactionDate, '%Y-%m') AS month,
		        fr.type AS type,
		        SUM(fr.amount) AS totalAmount
		    FROM FinancialRecord fr
		    WHERE fr.isDeleted = false
		    GROUP BY FUNCTION('DATE_FORMAT', fr.transactionDate, '%Y-%m'), fr.type
		    ORDER BY month
		""")
		List<MonthlyTrendProjection> getMonthlyTrends();
	
	@Query("""
		    SELECT COALESCE(SUM(fr.amount), 0)
		    FROM FinancialRecord fr
		    WHERE fr.type = 'INCOME'
		      AND fr.isDeleted = false
		      AND fr.createdBy.id = :userId
		""")
		BigDecimal getTotalIncomeByUser(UUID userId);
	
	@Query("""
		    SELECT COALESCE(SUM(fr.amount), 0)
		    FROM FinancialRecord fr
		    WHERE fr.type = 'EXPENSE'
		      AND fr.isDeleted = false
		      AND fr.createdBy.id = :userId
		""")
		BigDecimal getTotalExpenseByUser(UUID userId);
	
	
	@Query("""
		    SELECT
		        u.userName AS userName,
		        fr.category AS category,
		        fr.type AS type,
		        SUM(fr.amount) AS totalAmount,
		        COUNT(fr) AS transactionCount
		    FROM FinancialRecord fr
		    JOIN fr.createdBy u
		    WHERE fr.isDeleted = false
		    AND (:userId IS NULL OR u.id = :userId)
		    GROUP BY u.id, u.userName, fr.category, fr.type
		    ORDER BY totalAmount DESC
		""")
		List<UserCategoryBreakdownProjection> getCategoryBreakdownPerUser(@Param("userId") UUID userId);

		@Query("""
		    SELECT
		        u.userName AS userName,
		        u.fullName AS fullName,
		        SUM(fr.amount) AS totalExpense,
		        COUNT(fr) AS transactionCount,
		        AVG(fr.amount) AS averageTransactionAmount
		    FROM FinancialRecord fr
		    JOIN fr.createdBy u
		    WHERE fr.isDeleted = false
		    AND fr.type = 'EXPENSE'
		    GROUP BY u.id, u.userName, u.fullName
		    ORDER BY totalExpense DESC
		    LIMIT :limit
		""")
		List<TopSpenderProjection> getTopSpenders(@Param("limit") int limit);
	
	
	
	
	
}
