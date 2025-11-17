package com.example.backend.shared.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes do ObjectUtils")
class ObjectUtilsTest {

    @Test
    @DisplayName("Deve executar consumer quando valor não for nulo")
    void testUpdateIfPresent_WithNonNullValue() {
        List<String> result = new ArrayList<>();
        String value = "Teste";

        ObjectUtils.updateIfPresent(result::add, value);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo("Teste");
    }

    @Test
    @DisplayName("Não deve executar consumer quando valor for nulo")
    void testUpdateIfPresent_WithNullValue() {
        List<String> result = new ArrayList<>();
        String value = null;

        ObjectUtils.updateIfPresent(result::add, value);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve atualizar campo de objeto quando valor não for nulo")
    void testUpdateIfPresent_UpdateObjectField() {
        TestObject obj = new TestObject();
        String newValue = "Novo Valor";

        ObjectUtils.updateIfPresent(obj::setName, newValue);

        assertThat(obj.getName()).isEqualTo("Novo Valor");
    }

    @Test
    @DisplayName("Não deve atualizar campo de objeto quando valor for nulo")
    void testUpdateIfPresent_DontUpdateWhenNull() {
        TestObject obj = new TestObject();
        obj.setName("Valor Original");
        String newValue = null;

        ObjectUtils.updateIfPresent(obj::setName, newValue);

        assertThat(obj.getName()).isEqualTo("Valor Original");
    }

    @Test
    @DisplayName("Deve funcionar com tipos numéricos")
    void testUpdateIfPresent_WithNumbers() {
        AtomicInteger counter = new AtomicInteger(0);
        Integer value = 42;

        ObjectUtils.updateIfPresent(counter::set, value);

        assertThat(counter.get()).isEqualTo(42);
    }

    @Test
    @DisplayName("Deve funcionar com boolean")
    void testUpdateIfPresent_WithBoolean() {
        TestObject obj = new TestObject();
        Boolean value = true;

        ObjectUtils.updateIfPresent(obj::setActive, value);

        assertThat(obj.isActive()).isTrue();
    }

    @Test
    @DisplayName("Não deve executar consumer com boolean nulo")
    void testUpdateIfPresent_WithNullBoolean() {
        TestObject obj = new TestObject();
        obj.setActive(false);
        Boolean value = null;

        ObjectUtils.updateIfPresent(obj::setActive, value);

        assertThat(obj.isActive()).isFalse();
    }

    private static class TestObject {
        private String name;
        private boolean active;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
}