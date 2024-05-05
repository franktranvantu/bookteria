package com.franktranvantu.bookteria.identity.configuration;

import static com.franktranvantu.bookteria.identity.exception.ServiceStatusCode.UNAUTHENTICATED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franktranvantu.bookteria.identity.dto.response.ServiceResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        final var unauthenticated = UNAUTHENTICATED;
        response.setStatus(unauthenticated.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        final var serviceResponse = ServiceResponse.builder()
                .code(unauthenticated.getCode())
                .message(unauthenticated.getMessage())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(serviceResponse));
        response.flushBuffer();
    }
}
