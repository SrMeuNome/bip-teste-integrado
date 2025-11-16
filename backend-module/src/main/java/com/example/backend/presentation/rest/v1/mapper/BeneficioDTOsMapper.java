package com.example.backend.presentation.rest.v1.mapper;

import com.example.backend.presentation.rest.v1.dto.in.CreateBeneficioDTO;
import com.example.backend.presentation.rest.v1.dto.in.UpdateBeneficioDTO;
import com.example.backend.presentation.rest.v1.dto.out.BeneficioResponseDTO;
import com.example.domain.model.Beneficio;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BeneficioDTOsMapper {

    public Beneficio toBeneficio(CreateBeneficioDTO createBeneficioDTO) {
        String nome = createBeneficioDTO.getNome();
        String descricao = createBeneficioDTO.getDescricao();
        BigDecimal valor = createBeneficioDTO.getValor();
        Boolean ativo = createBeneficioDTO.getAtivo();

        Beneficio beneficio = new Beneficio();

        beneficio.setNome(nome);
        beneficio.setDescricao(descricao);
        beneficio.setValor(valor);
        beneficio.setAtivo(ativo);

        return beneficio;
    }

    public Beneficio toBeneficio(UpdateBeneficioDTO updateBeneficioDTO) {
        String nome = updateBeneficioDTO.getNome();
        String descricao = updateBeneficioDTO.getDescricao();
        BigDecimal valor = updateBeneficioDTO.getValor();
        Boolean ativo = updateBeneficioDTO.getAtivo();

        Beneficio beneficio = new Beneficio();

        beneficio.setNome(nome);
        beneficio.setDescricao(descricao);
        beneficio.setValor(valor);
        beneficio.setAtivo(ativo);

        return beneficio;
    }

    public BeneficioResponseDTO toBeneficioResponseDTO(Beneficio beneficio) {

        Long id = beneficio.getId();
        String nome = beneficio.getNome();
        String descricao = beneficio.getDescricao();
        BigDecimal valor = beneficio.getValor();
        Boolean ativo = beneficio.getAtivo();

        BeneficioResponseDTO beneficioResponseDTO = new BeneficioResponseDTO();

        beneficioResponseDTO.setId(id);
        beneficioResponseDTO.setNome(nome);
        beneficioResponseDTO.setDescricao(descricao);
        beneficioResponseDTO.setValor(valor);
        beneficioResponseDTO.setAtivo(ativo);

        return beneficioResponseDTO;
    }
}
