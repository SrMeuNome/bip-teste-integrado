package com.example.ejb;

import com.example.persistence.repository.jpa.entity.BeneficioEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeneficioEjbServiceTest {

    @Mock
    private EntityManager em;

    @InjectMocks
    private BeneficioEjbService service;

    private BeneficioEntity ativo1;
    private BeneficioEntity ativo2;

    @BeforeEach
    void setup() {
        ativo1 = new BeneficioEntity();
        ativo1.setId(1L);
        ativo1.setValor(new BigDecimal("100.00"));
        ativo1.setAtivo(true);
        ativo1.setNome("Benefício 1");

        ativo2 = new BeneficioEntity();
        ativo2.setId(2L);
        ativo2.setValor(new BigDecimal("50.00"));
        ativo2.setAtivo(true);
        ativo2.setNome("Benefício 2");
    }

    @Test
    void deveTransferirComSucesso() {
        when(em.find(BeneficioEntity.class, 1L, LockModeType.PESSIMISTIC_WRITE))
                .thenReturn(ativo1);
        when(em.find(BeneficioEntity.class, 2L, LockModeType.PESSIMISTIC_WRITE))
                .thenReturn(ativo2);

        service.transfer(1L, 2L, new BigDecimal("10.00"));

        assertThat(ativo1.getValor()).isEqualByComparingTo("90.00");
        assertThat(ativo2.getValor()).isEqualByComparingTo("60.00");

        verify(em).merge(ativo1);
        verify(em).merge(ativo2);
    }

    @Test
    void deveLancarErroSeValorInvalido() {
        assertThatThrownBy(() -> service.transfer(1L, 2L, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positivo");
    }

    @Test
    void deveLancarErroSeTransferenciaParaMesmoBeneficio() {
        assertThatThrownBy(() -> service.transfer(1L, 1L, new BigDecimal("10")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("mesmo benefício");
    }

    @Test
    void deveLancarErroSeBeneficioOrigemNaoEncontrado() {
        when(em.find(BeneficioEntity.class, 1L, LockModeType.PESSIMISTIC_WRITE))
                .thenReturn(null);
        when(em.find(BeneficioEntity.class, 2L, LockModeType.PESSIMISTIC_WRITE))
                .thenReturn(ativo2);

        assertThatThrownBy(() -> service.transfer(1L, 2L, new BigDecimal("10")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("origem não encontrado");
    }

    @Test
    void deveLancarErroSeBeneficioDestinoNaoEncontrado() {
        when(em.find(BeneficioEntity.class, 1L, LockModeType.PESSIMISTIC_WRITE))
                .thenReturn(ativo1);
        when(em.find(BeneficioEntity.class, 2L, LockModeType.PESSIMISTIC_WRITE))
                .thenReturn(null);

        assertThatThrownBy(() -> service.transfer(1L, 2L, new BigDecimal("10")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("destino não encontrado");
    }

    @Test
    void deveLancarErroSeOrigemInativo() {
        ativo1.setAtivo(false);

        when(em.find(BeneficioEntity.class, 1L, LockModeType.PESSIMISTIC_WRITE))
                .thenReturn(ativo1);
        when(em.find(BeneficioEntity.class, 2L, LockModeType.PESSIMISTIC_WRITE))
                .thenReturn(ativo2);

        assertThatThrownBy(() -> service.transfer(1L, 2L, new BigDecimal("10")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("inativo");
    }

    @Test
    void deveLancarErroSeDestinoInativo() {
        ativo2.setAtivo(false);

        when(em.find(BeneficioEntity.class, 1L, LockModeType.PESSIMISTIC_WRITE))
                .thenReturn(ativo1);
        when(em.find(BeneficioEntity.class, 2L, LockModeType.PESSIMISTIC_WRITE))
                .thenReturn(ativo2);

        assertThatThrownBy(() -> service.transfer(1L, 2L, new BigDecimal("10")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("inativo");
    }
}
