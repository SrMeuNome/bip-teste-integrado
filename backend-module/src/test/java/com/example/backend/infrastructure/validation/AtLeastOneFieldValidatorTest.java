package com.example.backend.infrastructure.validation;

import com.seu.projeto.infrastructure.validation.AtLeastOneFieldValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AtLeastOneFieldValidator")
class AtLeastOneFieldValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    private com.seu.projeto.infrastructure.validation.AtLeastOneFieldValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AtLeastOneFieldValidator();
    }

    @Test
    @DisplayName("Deve retornar false quando objeto for nulo")
    void testIsValid_NullObject() {
        boolean result = validator.isValid(null, context);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Deve retornar true quando pelo menos um campo não for nulo")
    void testIsValid_AtLeastOneFieldNotNull() {
        TestDTO dto = new TestDTO();
        dto.setName("Teste");

        boolean result = validator.isValid(dto, context);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando todos os campos forem nulos")
    void testIsValid_AllFieldsNull() {
        TestDTO dto = new TestDTO();

        boolean result = validator.isValid(dto, context);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Deve retornar true quando múltiplos campos não forem nulos")
    void testIsValid_MultipleFieldsNotNull() {
        TestDTO dto = new TestDTO();
        dto.setName("Teste");
        dto.setValue(new BigDecimal("100.00"));
        dto.setActive(true);

        boolean result = validator.isValid(dto, context);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Deve retornar true quando apenas um campo do meio não for nulo")
    void testIsValid_MiddleFieldNotNull() {
        TestDTO dto = new TestDTO();
        dto.setValue(new BigDecimal("50.00"));

        boolean result = validator.isValid(dto, context);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Deve retornar true quando último campo não for nulo")
    void testIsValid_LastFieldNotNull() {
        TestDTO dto = new TestDTO();
        dto.setActive(false);

        boolean result = validator.isValid(dto, context);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Deve ignorar campos serialVersionUID")
    void testIsValid_IgnoreSerialVersionUID() {
        TestDTOWithSerial dto = new TestDTOWithSerial();
        // Todos os campos exceto serialVersionUID são nulos

        boolean result = validator.isValid(dto, context);

        assertThat(result).isFalse();
    }

    // Classes auxiliares para testes
    private static class TestDTO {
        private String name;
        private BigDecimal value;
        private Boolean active;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getValue() {
            return value;
        }

        public void setValue(BigDecimal value) {
            this.value = value;
        }

        public Boolean getActive() {
            return active;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }
    }

    private static class TestDTOWithSerial {
        @SuppressWarnings("unused")
        private static final long serialVersionUID = 1L;

        private String name;
        private BigDecimal value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getValue() {
            return value;
        }

        public void setValue(BigDecimal value) {
            this.value = value;
        }
    }
}
