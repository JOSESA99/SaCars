package com.sacars.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth

            // Rutas públicas del frontend
            .requestMatchers("/", "/index", "/home", "/catalogo",
                             "/css/**", "/js/**", "/img/**", "/webjars/**").permitAll()

            // Rutas públicas del API para login/registro del cliente
            .requestMatchers("/api/auth/**").permitAll()

            // API que solo funciona si el cliente está logueado
            .requestMatchers("/api/cliente/**").authenticated()
            .requestMatchers("/api/compras/**").authenticated()
            .requestMatchers("/api/carrito/**").authenticated()

            // Zona administrativa
            .requestMatchers("/admin/**").hasRole("ADMIN")

            // Cualquier otra ruta requiere autenticación
            .anyRequest().authenticated()
        )

        // Login del ADMIN (solo backend)
        .formLogin(login -> login
            .loginPage("/auth/login")
            .defaultSuccessUrl("/admin/dashboard", true)
            .permitAll()
        )

        .logout(logout -> logout
            .logoutSuccessUrl("/")
            .permitAll()
        );

    return http.build();
}


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
