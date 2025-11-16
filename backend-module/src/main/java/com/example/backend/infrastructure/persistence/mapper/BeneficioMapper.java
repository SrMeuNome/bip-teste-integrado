package com.example.backend.infrastructure.persistence.mapper;

import com.example.domain.model.Beneficio;
import com.example.persistence.repository.jpa.entity.BeneficioEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BeneficioMapper {

    public Beneficio toBeneficio(BeneficioEntity beneficioEntity) {
        Long id = beneficioEntity.getId();
        String nome = beneficioEntity.getNome();
        String descricao = beneficioEntity.getDescricao();
        BigDecimal valor = beneficioEntity.getValor();
        Boolean ativo = beneficioEntity.getAtivo();
        Long version = beneficioEntity.getVersion();

        Beneficio beneficio = new Beneficio();

        beneficio.setId(id);
        beneficio.setNome(nome);
        beneficio.setDescricao(descricao);
        beneficio.setValor(valor);
        beneficio.setAtivo(ativo);
        beneficio.setVersion(version);

        return beneficio;
    }

    public BeneficioEntity toBriefStudentsEntity(Beneficio beneficio) {
        Long id = beneficio.getId();
        String nome = beneficio.getNome();
        String descricao = beneficio.getDescricao();
        BigDecimal valor = beneficio.getValor();
        Boolean ativo = beneficio.getAtivo();
        Long version = beneficio.getVersion();

        BeneficioEntity beneficioEntity = new BeneficioEntity();

        beneficioEntity.setId(id);
        beneficioEntity.setNome(nome);
        beneficioEntity.setDescricao(descricao);
        beneficioEntity.setValor(valor);
        beneficioEntity.setAtivo(ativo);
        beneficioEntity.setVersion(version);

        return beneficioEntity;
    }

}
