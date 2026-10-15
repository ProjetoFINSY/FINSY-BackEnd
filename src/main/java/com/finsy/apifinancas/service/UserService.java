//* */
package com.finsy.apifinancas.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.finsy.apifinancas.domain.PersonType;
import com.finsy.apifinancas.domain.User;
import com.finsy.apifinancas.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getAuthenticatedUser(OAuth2User principal, OAuth2AuthenticationToken authentication) {
        String providerId = principal.getAttribute("sub");
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        String provider = authentication.getAuthorizedClientRegistrationId();

        if (providerId == null || email == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "A conta do Google não informou os dados necessários");
        }

        return findOrCreate(name != null ? name : email, email, provider, providerId);
    }

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