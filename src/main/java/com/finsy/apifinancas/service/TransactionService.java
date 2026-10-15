package com.finsy.apifinancas.service;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.finsy.apifinancas.repository.TransactionRepository;
import com.finsy.apifinancas.domain.Transaction;
import com.finsy.apifinancas.domain.TransactionType;
import com.finsy.apifinancas.domain.User;
import com.finsy.apifinancas.dto.TransactionRequestDTO;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    /** Chamado pelo TransactionController (GET /transactions). Lista só as transações do usuário logado. */
    public List<Transaction> findAllByUser(User user) {
        return transactionRepository.findByUserIdOrderByDateDesc(user.getId());
    }

    /**
     * Chamado pelo TransactionController e por update()/delete() aqui.
     * Busca pelo id GARANTINDO que a transação pertence ao usuário logado.
     * Se não existir ou for de outro usuário, responde 404 — nunca revela que o registro existe.
     */
    public Transaction findByIdAndUser(Long id, User user) {
        return transactionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transação não encontrada"));
    }

    /** Chamado pelo TransactionController (POST /transactions). Cria uma nova transação para o usuário logado. */
    public Transaction create(TransactionRequestDTO dto, User user) {
        Transaction transaction = new Transaction();
        apply(transaction, dto);
        transaction.setUser(user);
        return transactionRepository.save(transaction);
    }

    /** Chamado pelo TransactionController (PUT /transactions/{id}). Atualiza uma transação já existente do usuário. */
    public Transaction update(Long id, TransactionRequestDTO dto, User user) {
        Transaction transaction = findByIdAndUser(id, user);
        apply(transaction, dto);
        return transactionRepository.save(transaction);
    }

    /** Chamado pelo TransactionController (DELETE /transactions/{id}). Remove uma transação do usuário. */
    public void delete(Long id, User user) {
        transactionRepository.delete(findByIdAndUser(id, user));
    }

    /**
     * Chamado só por create() e update(). Valida o DTO, copia os dados para a entidade e decide
     * qual coluna de valor recebe o valor enviado (vl_recebido para receita, vl_gasto para
     * despesa), zerando a outra.
     */
    private void apply(Transaction transaction, TransactionRequestDTO dto) {
        validate(dto);

        transaction.setType(dto.type());
        transaction.setDate(dto.date());
        transaction.setItem(dto.item());
        transaction.setExpenseCategory(
                dto.type() == TransactionType.EXPENSE ? dto.expenseCategory() : null);

        if (dto.type() == TransactionType.INCOME) {
            transaction.setReceivedAmount(dto.amount());
            transaction.setSpentAmount(null);
        } else {
            transaction.setSpentAmount(dto.amount());
            transaction.setReceivedAmount(null);
        }
    }

    /**
     * Chamado só por apply(). Confere os campos obrigatórios na mão (sem dependência de
     * validação) e responde 400 com uma mensagem clara no primeiro erro encontrado.
     */
    private void validate(TransactionRequestDTO dto) {
        if (dto.type() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o tipo da transação");
        }
        if (dto.date() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a data");
        }
        if (dto.item() == null || dto.item().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o item");
        }
        if (dto.amount() == null || dto.amount().signum() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor deve ser maior que zero");
        }
    }
}
