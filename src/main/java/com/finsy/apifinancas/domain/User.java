package com.finsy.apifinancas.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(
    name = "tb_usuario",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_usuario_provider_id",
        columnNames = {"provider", "id_provider"}
    )
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nm_usuario", nullable = false)
    private String name;

    @Column(name = "ds_email", nullable = false, unique = true)
    private String email;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "id_provider", nullable = false)
    private String providerId;

    @Column(name = "dt_nascimento")
    private LocalDate birthDate;

    @Column(name = "ds_endereco")
    private String address;

    @Column(name = "nr_telefone")
    private String phone;

    @Column(name = "nr_cep")
    private String zipCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "tp_cadastro")
    private PersonType personType;

    @Column(name = "nr_cpf", unique = true)
    private String cpf;

    @Column(name = "nr_cnpj", unique = true)
    private String cnpj;
}