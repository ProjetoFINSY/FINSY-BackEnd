package com.finsy.apifinancas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finsy.apifinancas.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserIdOrderByDateDesc(Long userId);

    // Garante a posse: só devolve a transação se ela for do usuário informado.
    Optional<Transaction> findByIdAndUserId(Long id, Long userId);
}