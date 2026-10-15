package com.finsy.apifinancas.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.finsy.apifinancas.domain.TransactionType;

public record TransactionRequestDTO(
    TransactionType type,
    LocalDate date,
    String item,
    String expenseCategory,
    BigDecimal amount) {

}
