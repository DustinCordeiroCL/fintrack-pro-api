package com.dustin.fintrack.dto.v1;

import com.dustin.fintrack.model.Transaction;
import com.dustin.fintrack.model.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDTO {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDateTime date;
    private TransactionType type;
    private CategoryDTO category;

    public TransactionDTO(Transaction entity) {
        this.id = entity.getId();
        this.description = entity.getDescription();
        this.amount = entity.getAmount();
        this.date = entity.getDate();
        this.type = entity.getType();
        // Mapping the internal Category to CategoryDTO
        this.category = new CategoryDTO(entity.getCategory());
    }
}