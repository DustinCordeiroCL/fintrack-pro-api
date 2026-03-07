package com.dustin.fintrack.dto.v1.response;

import com.dustin.fintrack.model.Transaction;
import com.dustin.fintrack.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDateTime date;
    private TransactionType type;
    private Integer dueDay;
    private Boolean isPaid;
    private CategoryResponseDTO category;

    public TransactionResponseDTO(Transaction entity) {
        this.id = entity.getId();
        this.description = entity.getDescription();
        this.amount = entity.getAmount();
        this.date = entity.getDate();
        this.type = entity.getType();
        this.dueDay = entity.getDueDay();
        this.isPaid = entity.getIsPaid();
        this.category = new CategoryResponseDTO(entity.getCategory());
    }
}