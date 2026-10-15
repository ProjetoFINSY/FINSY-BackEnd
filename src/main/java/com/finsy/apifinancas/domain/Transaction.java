package com.finsy.apifinancas.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_transacao")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transacao")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tp_transacao", nullable = false)
    private TransactionType type;

    @Column(name = "dt_registro", nullable = false)
    private LocalDate date;

    @Column(name = "ds_item", nullable = false)
    private String item;

    // Só é preenchido quando type = EXPENSE. Texto livre por enquanto;
    // pode virar um enum ou uma tabela própria de categorias no futuro.
    @Column(name = "tp_despesa")
    private String expenseCategory;

    @Column(name = "vl_recebido", precision = 19, scale = 2)
    private BigDecimal receivedAmount;

    @Column(name = "vl_gasto", precision = 19, scale = 2)
    private BigDecimal spentAmount;

    // 1 usuário : N transações
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private User user;
}
