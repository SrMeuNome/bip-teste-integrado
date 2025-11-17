package com.example.backend.presentation.rest.v1.controller;

import com.example.backend.application.service.BeneficioService;
import com.example.backend.presentation.rest.v1.dto.in.CreateBeneficioDTO;
import com.example.backend.presentation.rest.v1.dto.in.TransferBeneficioDTO;
import com.example.backend.presentation.rest.v1.dto.in.UpdateBeneficioDTO;
import com.example.backend.presentation.rest.v1.dto.out.ApiResponse;
import com.example.backend.presentation.rest.v1.dto.out.BeneficioResponseDTO;
import com.example.backend.presentation.rest.v1.mapper.BeneficioDTOsMapper;
import com.example.domain.model.Beneficio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Log4j2
@RestController
@RequestMapping("v1/beneficios")
@CrossOrigin
@Tag(name = "Benefício", description = "Gerenciamento de benefícios")
public class BeneficioController {

    private final BeneficioService beneficioService;
    private final BeneficioDTOsMapper beneficioDTOsMapper;

    @Autowired
    public BeneficioController(
            BeneficioService beneficioService,
            BeneficioDTOsMapper beneficioDTOsMapper
    ) {
        this.beneficioService = beneficioService;
        this.beneficioDTOsMapper = beneficioDTOsMapper;
    }


    @GetMapping
    @Operation(summary = "Lista todos os Benefícios")
    public ResponseEntity<ApiResponse<List<BeneficioResponseDTO>>> listBeneficios() {
        List<BeneficioResponseDTO> beneficiosResponse = beneficioService
                .findAll()
                .stream()
                .map(beneficioDTOsMapper::toBeneficioResponseDTO)
                .toList();

        if (beneficiosResponse.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ApiResponse<List<BeneficioResponseDTO>> response = ApiResponse.<List<BeneficioResponseDTO>>builder()
                .data(beneficiosResponse)
                .message("Benefícios encontrados com sucesso.")
                .build();

        log.info("Benefícios encontrados com sucesso.");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Encontra um benefício pelo id")
    public ResponseEntity<ApiResponse<BeneficioResponseDTO>> getBeneficioById (@PathVariable(name = "id") @Valid Long id) {
        Beneficio beneficio = beneficioService.findById(id);

        ApiResponse<BeneficioResponseDTO> beneficiosResponse = ApiResponse.<BeneficioResponseDTO>builder()
                .data(beneficioDTOsMapper.toBeneficioResponseDTO(beneficio))
                .message("Benefício encontrado com sucesso.")
                .build();

        log.info("Benefício encontrado com sucesso. ID: {}", id);

        return ResponseEntity.ok(beneficiosResponse);
    }

    @PostMapping
    @Operation(summary = "Cria um novo benefício")
    public ResponseEntity<ApiResponse<BeneficioResponseDTO>> createBeneficio(
            @RequestBody @Valid CreateBeneficioDTO createBeneficioDTO
    ) {
        Beneficio beneficio = beneficioService.create(beneficioDTOsMapper.toBeneficio(createBeneficioDTO));

        ApiResponse<BeneficioResponseDTO> beneficiosResponse = ApiResponse.<BeneficioResponseDTO>builder()
                .data(beneficioDTOsMapper.toBeneficioResponseDTO(beneficio))
                .message("Benefício salvo com sucesso.")
                .build();

        log.info("Benefício salvo com sucesso.");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(beneficiosResponse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um benefício já existente")
    public ResponseEntity<ApiResponse<BeneficioResponseDTO>> updateBeneficio(
            @PathVariable(name = "id") @Valid Long id,
            @RequestBody @Valid UpdateBeneficioDTO updateBeneficioDTO
    ) {

        Beneficio beneficio = beneficioService.update(id, beneficioDTOsMapper.toBeneficio(updateBeneficioDTO));

        ApiResponse<BeneficioResponseDTO> beneficiosResponse = ApiResponse.<BeneficioResponseDTO>builder()
                .data(beneficioDTOsMapper.toBeneficioResponseDTO(beneficio))
                .message("Benefício atualizado com sucesso.")
                .build();

        log.info("Benefício atualizado com sucesso.");

        return ResponseEntity.ok(beneficiosResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete benefícios pelo id")
    public ResponseEntity<ApiResponse<Void>> deleteBeneficio(@PathVariable(name = "id") Long id) {
        beneficioService.delete(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .data(null)
                .message("Benefício deletado com sucesso.")
                .build();

        log.info("Benefício deletado com sucesso. ID: {}", id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/transferir")
    @Operation(summary = "Transfere valor de um benefício para outro")
    public ResponseEntity<ApiResponse<Void>> transferirBeneficio(
            @RequestBody @Valid TransferBeneficioDTO transferDTO
    ) {
        beneficioService.transferir(
                transferDTO.getFromId(),
                transferDTO.getToId(),
                transferDTO.getAmount()
        );

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .data(null)
                .message("Transferência realizada com sucesso.")
                .build();

        log.info("Transferência realizada: de {} para {} no valor de {}",
                transferDTO.getFromId(),
                transferDTO.getToId(),
                transferDTO.getAmount());

        return ResponseEntity.ok(response);
    }
}
