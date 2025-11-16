package com.example.backend.infrastructure.persistence.repository;

import com.example.persistence.repository.jpa.entity.BeneficioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficioRepository extends JpaRepository<BeneficioEntity,Long> {}
