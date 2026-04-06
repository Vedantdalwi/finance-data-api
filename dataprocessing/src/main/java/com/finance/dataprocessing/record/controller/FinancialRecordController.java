package com.finance.dataprocessing.record.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.finance.dataprocessing.common.response.ApiResponse;
import com.finance.dataprocessing.record.dto.FinancialRecordRequestDto;
import com.finance.dataprocessing.record.dto.FinancialRecordResponseDto;
import com.finance.dataprocessing.record.dto.FinancialRecordUpdateDto;
import com.finance.dataprocessing.record.enums.TransactionType;
import com.finance.dataprocessing.record.service.FinancialRecordService;
import com.finance.dataprocessing.user.entity.User;
import com.finance.dataprocessing.user.repository.UserRepository;
import com.finance.dataprocessing.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Tag(name = "Financial Records", description = "Manage financial transactions and records")
@RestController
@RequestMapping("/finance/records")
@RequiredArgsConstructor
public class FinancialRecordController {

    private final FinancialRecordService financialRecordService;
    
    private final UserRepository userRepository;

 
    @Tag(name = "Financial Records", description = "Manage financial transactions and records")

    @Operation(
            summary = "Create Financial Record",
            description = "Creates a new financial transaction record for a user"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Record created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FinancialRecordResponseDto.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input"
            )
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<FinancialRecordResponseDto>> createRecord(
            @Valid @RequestBody FinancialRecordRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
    	 User user = userRepository.findByEmail(userDetails.getUsername())
    	            .orElseThrow(() -> new RuntimeException("User not found"));

        FinancialRecordResponseDto response =
                financialRecordService.createRecord(request, user.getId());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Record created successfully", response, Instant.now())
        );
    }


    @Operation(
            summary = "Get Financial Record by ID",
            description = "Fetch a financial record using its unique ID"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Record fetched successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FinancialRecordResponseDto.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Record not found"
            )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER', 'ANALYST')")
    public ResponseEntity<ApiResponse<FinancialRecordResponseDto>> getRecordById(
            @Parameter(description = "Financial Record ID")
            @PathVariable UUID id
    ) {

        FinancialRecordResponseDto response =
                financialRecordService.getRecordById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Record fetched successfully", response, Instant.now())
        );
    }


    @Operation(
            summary = "Get Financial Records",
            description = "Fetch financial records with filters like date range, type, category, search and pagination"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Records fetched successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = FinancialRecordResponseDto.class)
                            )
                    )
            )
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER', 'ANALYST')")
    public ResponseEntity<ApiResponse<List<FinancialRecordResponseDto>>> getRecords(

            @Parameter(description = "User ID whose records are to be fetched")
            @RequestParam UUID userId,

            @Parameter(description = "Start date filter (inclusive)")
            @RequestParam(required = false) Instant startDate,

            @Parameter(description = "End date filter (inclusive)")
            @RequestParam(required = false) Instant endDate,

            @Parameter(description = "Transaction type filter (INCOME / EXPENSE)")
            @RequestParam(required = false) TransactionType type,

            @Parameter(description = "Category filter")
            @RequestParam(required = false) String category,

            @Parameter(description = "Search keyword for category or description")
            @RequestParam(required = false) String search,

            @Parameter(description = "Page number for pagination (default = 0)")
            @RequestParam(defaultValue = "0") Integer pageNo
    ) {

        List<FinancialRecordResponseDto> records =
                financialRecordService.getRecords(
                        userId, startDate, endDate, type, category, search, pageNo
                );

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Records fetched successfully", records, Instant.now())
        );
    }


    @Operation(
            summary = "Update Financial Record",
            description = "Updates an existing financial record. Only non-null fields will be updated"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Record updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FinancialRecordResponseDto.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Record not found"
            )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<FinancialRecordResponseDto>> updateRecord(
            @Parameter(description = "Financial Record ID")
            @PathVariable UUID id,
            @RequestBody FinancialRecordUpdateDto request
    ) {

        FinancialRecordResponseDto response =
                financialRecordService.updateRecord(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Record updated successfully", response, Instant.now())
        );
    }


    @Operation(
            summary = "Delete Financial Record",
            description = "Soft deletes a financial record by marking it as deleted"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Record deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Record not found"
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRecord(
            @Parameter(description = "Financial Record ID")
            @PathVariable UUID id
    ) {

        financialRecordService.deleteRecord(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Record deleted successfully", null, Instant.now())
        );
    }
}
