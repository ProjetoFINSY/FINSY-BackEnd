package com.finsy.apifinancas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.finsy.apifinancas.domain.User;
import com.finsy.apifinancas.dto.UserResponseDTO;
import com.finsy.apifinancas.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResponseDTO getCurrentUser(
            @AuthenticationPrincipal OAuth2User principal,
            OAuth2AuthenticationToken authentication) {

        User user = userService.getAuthenticatedUser(principal, authentication);
        String pictureUrl = principal.getAttribute("picture");
        return new UserResponseDTO(user, pictureUrl);
    }
}