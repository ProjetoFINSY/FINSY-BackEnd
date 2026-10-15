package com.finsy.apifinancas.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.finsy.apifinancas.domain.TransactionType;
import com.finsy.apifinancas.domain.Transaction;

public record TransactionResponseDTO(
    Long id,
        TransactionType type,
        LocalDate date,
        String item,
        String expenseCategory,
        BigDecimal amount
) {
    public TransactionResponseDTO(Transaction transaction) {
        this(
            transaction.getId(),
            transaction.getType(),
            transaction.getDate(),
            transaction.getItem(),
            transaction.getExpenseCategory(),
            transaction.getType() == TransactionType.INCOME
                ? transaction.getReceivedAmount()
                : transaction.getSpentAmount()
        );
    }
}