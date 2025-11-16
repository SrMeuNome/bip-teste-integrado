package com.example.backend.infrastructure.persistence.adapter;

import com.example.backend.application.excepiton.BeneficioNotFoundException;
import com.example.backend.application.excepiton.TransferFailedException;
import com.example.backend.infrastructure.persistence.mapper.BeneficioMapper;
import com.example.backend.infrastructure.persistence.repository.BeneficioRepository;
import com.example.domain.model.Beneficio;
import com.example.domain.repository.BeneficioRepositoryInterface;
import com.example.ejb.BeneficioEjbServiceRemote;
import com.example.persistence.repository.jpa.entity.BeneficioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class BeneficioRepositoryAdapter implements BeneficioRepositoryInterface {

    private final BeneficioRepository repository;
    private final BeneficioEjbServiceRemote ejbServiceRemote;
    private final BeneficioMapper mapper;

    @Autowired
    public BeneficioRepositoryAdapter(
            BeneficioRepository repository,
            BeneficioEjbServiceRemote ejbServiceRemote,
            BeneficioMapper mapper
    ) {
        this.repository = repository;
        this.ejbServiceRemote = ejbServiceRemote;
        this.mapper = mapper;
    }

    @Override
    public Optional<Beneficio> findById(Long beneficioId) {
        return repository.findById(beneficioId)
                .map(mapper::toBeneficio);
    }

    @Override
    public List<Beneficio> findAll() {
        List<BeneficioEntity> entities = repository.findAll();

        if (entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(mapper::toBeneficio)
                .toList();
    }

    @Override
    public Beneficio create(Beneficio beneficio) {
        BeneficioEntity entity = mapper.toBriefStudentsEntity(beneficio);
        BeneficioEntity saved = repository.save(entity);
        return mapper.toBeneficio(saved);
    }

    @Override
    public boolean delete(Long beneficioId) {
        if (!repository.existsById(beneficioId)) {
            return false;
        }

        repository.deleteById(beneficioId);
        return true;
    }

    @Override
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        ejbServiceRemote.transfer(fromId, toId, amount);
    }
}
