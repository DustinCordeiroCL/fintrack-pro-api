package com.dustin.fintrack.dto.v1.request;

import com.dustin.fintrack.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDTO {

        @NotBlank(message = "Description is required")
        private String description;

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;

        @NotNull(message = "Date is required")
        private LocalDateTime date;

        @NotNull(message = "Type is required")
        private TransactionType type;

        @NotNull(message = "Category ID is required")
        private Long categoryId;
}