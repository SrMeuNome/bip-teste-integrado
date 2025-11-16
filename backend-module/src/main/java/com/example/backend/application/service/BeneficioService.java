package com.example.backend.application.service;

import com.example.backend.application.excepiton.BeneficioNotFoundException;
import com.example.backend.shared.utils.ObjectUtils;
import com.example.domain.model.Beneficio;
import com.example.domain.repository.BeneficioRepositoryInterface;
import com.example.persistence.repository.jpa.entity.BeneficioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BeneficioService {

    private final BeneficioRepositoryInterface beneficioRepository;

    @Autowired
    public BeneficioService(BeneficioRepositoryInterface beneficioRepository) {
        this.beneficioRepository = beneficioRepository;
    }

    @Transactional(readOnly = true)
    public Beneficio findById(Long id) throws BeneficioNotFoundException {
        return beneficioRepository
                .findById(id)
                .orElseThrow(() -> new BeneficioNotFoundException("O benefício de id " + id + " não foi encontrado."));
    }

    @Transactional(readOnly = true)
    public List<Beneficio> findAll() {
        return beneficioRepository.findAll();
    }

    @Transactional
    public Beneficio create(Beneficio beneficio) {
        return beneficioRepository.create(beneficio);
    }

    @Transactional
    public Beneficio update(Long beneficioId, Beneficio dadosAtualizados) {
        Beneficio beneficioExistente  = beneficioRepository.findById(beneficioId)
                .orElseThrow(() -> new BeneficioNotFoundException("O benefício de id " + beneficioId + " não foi encontrado."));

        ObjectUtils.updateIfPresent(beneficioExistente::setNome, dadosAtualizados.getNome());
        ObjectUtils.updateIfPresent(beneficioExistente::setDescricao, dadosAtualizados.getDescricao());
        ObjectUtils.updateIfPresent(beneficioExistente::setValor, dadosAtualizados.getValor());
        ObjectUtils.updateIfPresent(beneficioExistente::setAtivo, dadosAtualizados.getAtivo());
        ObjectUtils.updateIfPresent(beneficioExistente::setNome, dadosAtualizados.getNome());
        ObjectUtils.updateIfPresent(beneficioExistente::setNome, dadosAtualizados.getNome());

        return beneficioRepository.create(beneficioExistente);
    }

    @Transactional
    public void delete(Long id) {
        boolean deleted = beneficioRepository.delete(id);

        if (!deleted) {
            throw new BeneficioNotFoundException("O benefício de id " + id + " não foi encontrado.");
        }
    }

    public void transferir(Long fromId, Long toId, BigDecimal amount) {
        beneficioRepository.transfer(fromId, toId, amount);
    }
}
