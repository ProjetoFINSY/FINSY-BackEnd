package com.finsy.apifinancas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finsy.apifinancas.domain.User;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}