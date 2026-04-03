package com.finance.dataprocessing.record.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.finance.dataprocessing.record.entity.FinancialRecord;

public interface FinancialRecordRepository extends JpaRepository< FinancialRecord, UUID>, JpaSpecificationExecutor<FinancialRecord> {

}
