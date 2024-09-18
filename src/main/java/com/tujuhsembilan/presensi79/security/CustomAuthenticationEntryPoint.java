package com.tujuhsembilan.presensi79.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.util.MessageUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private MessageUtil messageUtil;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String errorMessage = messageUtil.get("application.error.unauthorized");
        String statusMessage = messageUtil.get("application.status.unauthorized");

        MessageResponse messageResponse = new MessageResponse(errorMessage, HttpServletResponse.SC_UNAUTHORIZED, statusMessage);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ObjectMapper mapper = new ObjectMapper();
        response.getOutputStream().println(mapper.writeValueAsString(messageResponse));
    }
}
