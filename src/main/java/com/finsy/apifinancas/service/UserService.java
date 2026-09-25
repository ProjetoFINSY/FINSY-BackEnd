package com.finsy.apifinancas.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.finsy.apifinancas.domain.PersonType;
import com.finsy.apifinancas.domain.User;
import com.finsy.apifinancas.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findOrCreate(String name, String email, String provider, String providerId) {
        return userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> create(name, email, provider, providerId));
    }

    private User create(String name, String email, String provider, String providerId) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setProvider(provider);
        user.setProviderId(providerId);
        user.setPersonType(PersonType.INDIVIDUAL);

        try {
            return userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            return userRepository.findByProviderAndProviderId(provider, providerId)
                    .orElseThrow(() -> e);
        }
    }
}