package com.finsy.apifinancas.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import com.finsy.apifinancas.service.TransactionService;
import com.finsy.apifinancas.service.UserService;
import com.finsy.apifinancas.domain.User;
import com.finsy.apifinancas.dto.TransactionResponseDTO;
import com.finsy.apifinancas.dto.TransactionRequestDTO;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    /** GET /transactions. Chamado pelo Angular (TransactionService.findAll) ao abrir a tela de transações. */
    @GetMapping
    public List<TransactionResponseDTO> findAll(
            @AuthenticationPrincipal OAuth2User principal,
            OAuth2AuthenticationToken authentication) {

        User user = userService.getAuthenticatedUser(principal, authentication);
        return transactionService.findAllByUser(user).stream()
                .map(TransactionResponseDTO::new)
                .toList();
    }

    /** GET /transactions/{id}. Chamado ao abrir uma transação específica. */
    @GetMapping("/{id}")
    public TransactionResponseDTO findById(
            @PathVariable Long id,
            @AuthenticationPrincipal OAuth2User principal,
            OAuth2AuthenticationToken authentication) {

        User user = userService.getAuthenticatedUser(principal, authentication);
        return new TransactionResponseDTO(transactionService.findByIdAndUser(id, user));
    }

    /** POST /transactions. Chamado pelo formulário de nova transação. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponseDTO create(
            @RequestBody TransactionRequestDTO dto,
            @AuthenticationPrincipal OAuth2User principal,
            OAuth2AuthenticationToken authentication) {

        User user = userService.getAuthenticatedUser(principal, authentication);
        return new TransactionResponseDTO(transactionService.create(dto, user));
    }

    /** PUT /transactions/{id}. Chamado pelo formulário ao editar uma transação existente. */
    @PutMapping("/{id}")
    public TransactionResponseDTO update(
            @PathVariable Long id,
            @RequestBody TransactionRequestDTO dto,
            @AuthenticationPrincipal OAuth2User principal,
            OAuth2AuthenticationToken authentication) {

        User user = userService.getAuthenticatedUser(principal, authentication);
        return new TransactionResponseDTO(transactionService.update(id, dto, user));
    }

    /** DELETE /transactions/{id}. Chamado pelo botão de excluir na listagem. */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal OAuth2User principal,
            OAuth2AuthenticationToken authentication) {

        User user = userService.getAuthenticatedUser(principal, authentication);
        transactionService.delete(id, user);
    }
}