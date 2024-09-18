package com.tujuhsembilan.presensi79.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.security.CustomAuthenticationEntryPoint;
import com.tujuhsembilan.presensi79.security.jwt.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Menambahkan endpoint yang tidak memerlukan token (public)
    private static final List<RequestMatcher> PUBLIC_ENDPOINT = Arrays.asList(
            new AntPathRequestMatcher("/employee/login"),
            new AntPathRequestMatcher("/superadmin/login"),
            new AntPathRequestMatcher("/admin-management/admins"),
            new AntPathRequestMatcher("/superadmin/register")
    );

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PUBLIC_ENDPOINT.toArray(new RequestMatcher[0]))
                        .permitAll() // Mengizinkan akses ke endpoint public tanpa autentikasi
                        .anyRequest().authenticated() // Permintaan lainnya memerlukan autentikasi
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless sessions
                .exceptionHandling(exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtAuthenticationFilter() {
        return new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain) throws ServletException, IOException {

                // Memeriksa apakah endpoint termasuk public endpoint yang tidak memerlukan token
                Boolean matched = PUBLIC_ENDPOINT.stream().anyMatch(matcher -> matcher.matches(request));
                if (matched) {
                    filterChain.doFilter(request, response);
                    return;
                }

                String authHeader = request.getHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String jwt = authHeader.substring(7);
                    try {
                        if (jwtUtils.validateJwtToken(jwt)) {
                            String username = jwtUtils.getUserNameFromJwtToken(jwt);
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    username, null, null);
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        } else {
                            // Token invalid
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            MessageResponse messageResponse = new MessageResponse("Unauthorized",
                                    HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "Invalid JWT token");
                            ObjectMapper mapper = new ObjectMapper();
                            response.getOutputStream().println(mapper.writeValueAsString(messageResponse));
                            return;
                        }
                    } catch (Exception e) {
                        System.out.println("JWT Validation Exception: " + e.getMessage());
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        MessageResponse messageResponse = new MessageResponse("Unauthorized",
                                HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "Invalid JWT token");
                        ObjectMapper mapper = new ObjectMapper();
                        response.getOutputStream().println(mapper.writeValueAsString(messageResponse));
                        return;
                    }
                }

                filterChain.doFilter(request, response);
            }
        };
    }
}
