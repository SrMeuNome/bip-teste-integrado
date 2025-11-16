package com.example.domain.repository;

import com.example.domain.model.Beneficio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BeneficioRepositoryInterface {
    Optional<Beneficio> findById(Long beneficioId);
    List<Beneficio> findAll();
    Beneficio create(Beneficio beneficio);
    boolean delete(Long beneficioId);
    void transfer(Long fromId, Long toId, BigDecimal amount);
}
