package com.dustin.fintrack.dto.v1.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategorySummaryDTO {
    private Long categoryId;
    private String categoryName;
    private BigDecimal totalAmount;
    private Long transactionCount;
    private BigDecimal percentageOfTotal;
}
