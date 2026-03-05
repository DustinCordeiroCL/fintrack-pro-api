package com.dustin.fintrack.controller.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceExceptionHandlerTest{
    @InjectMocks
    private ResourceExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @Test
    @DisplayName("Should handle ResourceNotFoundException and return 404 status")
    void resourceNotFoundTest() {
        Long id = 1L;
        ResourceNotFoundException ex = new ResourceNotFoundException(id);
        when(request.getRequestURI()).thenReturn("/api/v1/transactions/" + id);

        ResponseEntity<StandardError> response = handler.resourceNotFound(ex, request);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource Not Found", response.getBody().getError());
        assertEquals("/api/v1/transactions/1", response.getBody().getPath());
    }

    @Test
    @DisplayName("Should handle RuntimeException and return 400 status")
    void handleRuntimeExceptionTest() {
        RuntimeException ex = new RuntimeException("Generic Error");
        when(request.getRequestURI()).thenReturn("/api/v1/transactions");

        ResponseEntity<StandardError> response = handler.handleRuntimeException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Business Logic Error", response.getBody().getError());
        assertEquals("Generic Error", response.getBody().getMessage());
    }
}