package com.example.backend.presentation.rest.v1.controller.advice;

import com.example.backend.application.excepiton.EntityNotFoundException;
import com.example.backend.presentation.rest.v1.dto.out.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        when(request.getRequestURI()).thenReturn("/v1/beneficios");
    }

    @Test
    @DisplayName("Deve tratar EntityNotFoundException corretamente")
    void testHandleEntityNotFound() {
        EntityNotFoundException exception =
                new EntityNotFoundException("O benefício não foi encontrado", "Beneficio");

        ResponseEntity<ApiResponse<Void>> response =
                exceptionHandler.handleUserNotFund(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();

        assertThat(response.getBody().getMessage()).isEqualTo("Beneficio not found");

        assertThat(response.getBody().getErrors()).hasSize(1);
        assertThat(response.getBody().getErrors().get(0))
                .isEqualTo("O benefício não foi encontrado");
        assertThat(response.getBody().getData()).isNull();
    }

    @Test
    @DisplayName("Deve tratar EntityNotFoundException com entidade diferente")
    void testHandleEntityNotFound_DifferentEntity() {
        EntityNotFoundException exception =
                new EntityNotFoundException("Usuário com id 10 não encontrado", "Usuario");

        ResponseEntity<ApiResponse<Void>> response =
                exceptionHandler.handleUserNotFund(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();

        assertThat(response.getBody().getMessage()).isEqualTo("Usuario not found");

        assertThat(response.getBody().getErrors())
                .contains("Usuário com id 10 não encontrado");
    }

    @Test
    @DisplayName("Deve tratar MethodArgumentNotValidException com erros de campo")
    void testHandleMethodArgumentNotValid_WithFieldErrors() {
        MethodParameter methodParameter = mock(MethodParameter.class);

        FieldError fieldError1 = new FieldError("createBeneficioDTO", "nome", "não pode estar vazio");
        FieldError fieldError2 = new FieldError("createBeneficioDTO", "valor", "deve ser positivo");

        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));
        when(bindingResult.getGlobalErrors()).thenReturn(List.of());

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleMethodArgumentNotValid(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Validation error");
        assertThat(response.getBody().getErrors()).hasSize(2);
        assertThat(response.getBody().getErrors()).contains("nome: não pode estar vazio");
        assertThat(response.getBody().getErrors()).contains("valor: deve ser positivo");
        assertThat(response.getBody().getData()).isNull();
    }

    @Test
    @DisplayName("Deve tratar MethodArgumentNotValidException com erros globais")
    void testHandleMethodArgumentNotValid_WithGlobalErrors() {
        ObjectError globalError = new ObjectError("updateBeneficioDTO", "Pelo menos um campo deve ser preenchido");

        when(bindingResult.getFieldErrors()).thenReturn(List.of());
        when(bindingResult.getGlobalErrors()).thenReturn(List.of(globalError));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleMethodArgumentNotValid(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Validation error");
        assertThat(response.getBody().getErrors()).hasSize(1);
        assertThat(response.getBody().getErrors()).contains("Pelo menos um campo deve ser preenchido");
    }

    @Test
    @DisplayName("Deve tratar MethodArgumentNotValidException com múltiplos erros de campo e globais")
    void testHandleMethodArgumentNotValid_WithBothErrors() {
        FieldError fieldError = new FieldError("dto", "email", "formato inválido");
        ObjectError globalError = new ObjectError("dto", "Dados inconsistentes");

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        when(bindingResult.getGlobalErrors()).thenReturn(List.of(globalError));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleMethodArgumentNotValid(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrors()).hasSize(2);
        assertThat(response.getBody().getErrors()).contains("email: formato inválido");
        assertThat(response.getBody().getErrors()).contains("Dados inconsistentes");
    }

    @Test
    @DisplayName("Deve tratar ConstraintViolationException corretamente")
    void testHandleConstraintViolation() {
        Set<ConstraintViolation<?>> violations = new HashSet<>();

        ConstraintViolation<?> violation1 = mock(ConstraintViolation.class);
        Path path1 = mock(Path.class);
        when(path1.toString()).thenReturn("id");
        when(violation1.getPropertyPath()).thenReturn(path1);
        when(violation1.getMessage()).thenReturn("não pode ser nulo");

        ConstraintViolation<?> violation2 = mock(ConstraintViolation.class);
        Path path2 = mock(Path.class);
        when(path2.toString()).thenReturn("valor");
        when(violation2.getPropertyPath()).thenReturn(path2);
        when(violation2.getMessage()).thenReturn("deve ser maior que zero");

        violations.add(violation1);
        violations.add(violation2);

        ConstraintViolationException exception = new ConstraintViolationException(violations);

        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleConstraintViolation(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Validation error");
        assertThat(response.getBody().getErrors()).hasSize(2);
        assertThat(response.getBody().getData()).isNull();
    }

    @Test
    @DisplayName("Deve tratar ConstraintViolationException com única violação")
    void testHandleConstraintViolation_SingleViolation() {
        Set<ConstraintViolation<?>> violations = new HashSet<>();

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("nome");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("tamanho deve estar entre 1 e 100");

        violations.add(violation);

        ConstraintViolationException exception = new ConstraintViolationException(violations);

        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleConstraintViolation(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors()).hasSize(1);
        assertThat(response.getBody().getErrors().get(0)).contains("nome");
        assertThat(response.getBody().getErrors().get(0)).contains("tamanho deve estar entre 1 e 100");
    }

    @Test
    @DisplayName("Deve tratar Exception genérica corretamente")
    void testHandleGenericException() {
        Exception exception = new RuntimeException("Erro inesperado no sistema");

        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleGeneral(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Internal Server Error");
        assertThat(response.getBody().getErrors()).hasSize(1);
        assertThat(response.getBody().getErrors().get(0)).isEqualTo("Erro inesperado no sistema");
        assertThat(response.getBody().getData()).isNull();
    }

    @Test
    @DisplayName("Deve tratar NullPointerException como erro genérico")
    void testHandleGenericException_NullPointer() {
        Exception exception = new NullPointerException("Objeto não pode ser nulo");

        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleGeneral(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Internal Server Error");
        assertThat(response.getBody().getErrors()).contains("Objeto não pode ser nulo");
    }

    @Test
    @DisplayName("Deve tratar IllegalArgumentException como erro genérico")
    void testHandleGenericException_IllegalArgument() {
        Exception exception = new IllegalArgumentException("Argumento inválido fornecido");

        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleGeneral(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getErrors()).contains("Argumento inválido fornecido");
    }

    @Test
    @DisplayName("Deve incluir URI da requisição nos logs")
    void testExceptionHandling_LogsRequestURI() {
        EntityNotFoundException exception = new EntityNotFoundException("Test", "Test message");

        exceptionHandler.handleUserNotFund(exception, request);

        verify(request, atLeastOnce()).getRequestURI();
    }
}