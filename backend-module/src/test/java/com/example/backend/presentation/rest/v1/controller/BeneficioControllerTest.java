package com.example.backend.presentation.rest.v1.controller;

import com.example.backend.application.excepiton.BeneficioNotFoundException;
import com.example.backend.application.service.BeneficioService;
import com.example.backend.presentation.rest.v1.dto.in.CreateBeneficioDTO;
import com.example.backend.presentation.rest.v1.dto.in.TransferBeneficioDTO;
import com.example.backend.presentation.rest.v1.dto.in.UpdateBeneficioDTO;
import com.example.backend.presentation.rest.v1.dto.out.ApiResponse;
import com.example.backend.presentation.rest.v1.dto.out.BeneficioResponseDTO;
import com.example.backend.presentation.rest.v1.mapper.BeneficioDTOsMapper;
import com.example.domain.model.Beneficio;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do BeneficioController")
class BeneficioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BeneficioService beneficioService;

    @Mock
    private BeneficioDTOsMapper beneficioDTOsMapper;

    @InjectMocks
    private BeneficioController beneficioController;

    private ObjectMapper objectMapper;
    private Beneficio beneficio;
    private BeneficioResponseDTO beneficioResponseDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(beneficioController).build();
        objectMapper = new ObjectMapper();

        beneficio = new Beneficio();
        beneficio.setId(1L);
        beneficio.setNome("Vale Alimentação");
        beneficio.setDescricao("Benefício de alimentação");
        beneficio.setValor(new BigDecimal("500.00"));
        beneficio.setAtivo(true);

        beneficioResponseDTO = new BeneficioResponseDTO();
        beneficioResponseDTO.setId(1L);
        beneficioResponseDTO.setNome("Vale Alimentação");
        beneficioResponseDTO.setDescricao("Benefício de alimentação");
        beneficioResponseDTO.setValor(new BigDecimal("500.00"));
        beneficioResponseDTO.setAtivo(true);
    }

    @Test
    @DisplayName("Deve listar todos os benefícios com sucesso")
    void testListBeneficios_Success() throws Exception {
        List<Beneficio> beneficios = Arrays.asList(beneficio);
        when(beneficioService.findAll()).thenReturn(beneficios);
        when(beneficioDTOsMapper.toBeneficioResponseDTO(any(Beneficio.class)))
                .thenReturn(beneficioResponseDTO);

        mockMvc.perform(get("/v1/beneficios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].nome").value("Vale Alimentação"))
                .andExpect(jsonPath("$.message").value("Benefícios encontrados com sucesso."));

        verify(beneficioService, times(1)).findAll();
        verify(beneficioDTOsMapper, times(1)).toBeneficioResponseDTO(any(Beneficio.class));
    }

    @Test
    @DisplayName("Deve retornar 204 quando não houver benefícios")
    void testListBeneficios_Empty() throws Exception {
        when(beneficioService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/v1/beneficios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(beneficioService, times(1)).findAll();
        verify(beneficioDTOsMapper, never()).toBeneficioResponseDTO(any());
    }

    @Test
    @DisplayName("Deve buscar benefício por ID com sucesso")
    void testGetBeneficioById_Success() throws Exception {
        when(beneficioService.findById(1L)).thenReturn(beneficio);
        when(beneficioDTOsMapper.toBeneficioResponseDTO(beneficio)).thenReturn(beneficioResponseDTO);

        mockMvc.perform(get("/v1/beneficios/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("Vale Alimentação"))
                .andExpect(jsonPath("$.message").value("Benefício encontrado com sucesso."));

        verify(beneficioService, times(1)).findById(1L);
        verify(beneficioDTOsMapper, times(1)).toBeneficioResponseDTO(beneficio);
    }

    @Test
    @DisplayName("Deve criar um novo benefício com sucesso")
    void testCreateBeneficio_Success() throws Exception {
        CreateBeneficioDTO createDTO = new CreateBeneficioDTO();
        createDTO.setNome("Vale Transporte");
        createDTO.setDescricao("Benefício de transporte");
        createDTO.setValor(new BigDecimal("300.00"));
        createDTO.setAtivo(true);

        when(beneficioDTOsMapper.toBeneficio(any(CreateBeneficioDTO.class))).thenReturn(beneficio);
        when(beneficioService.create(any(Beneficio.class))).thenReturn(beneficio);
        when(beneficioDTOsMapper.toBeneficioResponseDTO(beneficio)).thenReturn(beneficioResponseDTO);

        mockMvc.perform(post("/v1/beneficios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.message").value("Benefício salvo com sucesso."));

        verify(beneficioService, times(1)).create(any(Beneficio.class));
        verify(beneficioDTOsMapper, times(1)).toBeneficio(any(CreateBeneficioDTO.class));
        verify(beneficioDTOsMapper, times(1)).toBeneficioResponseDTO(beneficio);
    }

    @Test
    @DisplayName("Deve atualizar um benefício com sucesso")
    void testUpdateBeneficio_Success() throws Exception {
        UpdateBeneficioDTO updateDTO = new UpdateBeneficioDTO();
        updateDTO.setNome("Vale Alimentação Atualizado");
        updateDTO.setValor(new BigDecimal("600.00"));

        when(beneficioDTOsMapper.toBeneficio(any(UpdateBeneficioDTO.class))).thenReturn(beneficio);
        when(beneficioService.update(eq(1L), any(Beneficio.class))).thenReturn(beneficio);
        when(beneficioDTOsMapper.toBeneficioResponseDTO(beneficio)).thenReturn(beneficioResponseDTO);

        mockMvc.perform(put("/v1/beneficios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.message").value("Benefício atualizado com sucesso."));

        verify(beneficioService, times(1)).update(eq(1L), any(Beneficio.class));
    }

    @Test
    @DisplayName("Deve deletar um benefício com sucesso")
    void testDeleteBeneficio_Success() throws Exception {
        doNothing().when(beneficioService).delete(1L);

        mockMvc.perform(delete("/v1/beneficios/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Benefício deletado com sucesso."));

        verify(beneficioService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Deve transferir valor entre benefícios com sucesso")
    void testTransferirBeneficio_Success() throws Exception {
        TransferBeneficioDTO transferDTO = new TransferBeneficioDTO();
        transferDTO.setFromId(1L);
        transferDTO.setToId(2L);
        transferDTO.setAmount(new BigDecimal("100.00"));

        doNothing().when(beneficioService).transferir(1L, 2L, new BigDecimal("100.00"));

        mockMvc.perform(post("/v1/beneficios/transferir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transferência realizada com sucesso."));

        verify(beneficioService, times(1)).transferir(1L, 2L, new BigDecimal("100.00"));
    }
}