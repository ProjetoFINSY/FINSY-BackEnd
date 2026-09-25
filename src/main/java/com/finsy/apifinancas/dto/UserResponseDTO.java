package com.finsy.apifinancas.dto;

import com.finsy.apifinancas.domain.User;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String provider,
        String providerId,
        String pictureUrl
) {

    public UserResponseDTO(User user, String pictureUrl) {
        this(user.getId(), user.getName(), user.getEmail(),
             user.getProvider(), user.getProviderId(), pictureUrl);
    }
}