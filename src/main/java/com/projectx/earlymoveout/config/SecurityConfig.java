package com.projectx.earlymoveout.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", "/index.html",
                    "/login.html", "/register.html",
                    "/login", "/register",
                    "/error",
                    "/css/**", "/js/**", "/images/**"
                ).permitAll()

                // ✅ Correct role names
                .requestMatchers("/faculty.html").hasRole("FACULTY")
                .requestMatchers("/hod.html").hasRole("HOD")
                .requestMatchers("/gate.html").hasRole("GATE")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .failureUrl("/login.html?error=true")

                // ✅ Redirect based on role
                .successHandler((request, response, authentication) -> {
                    String roles = authentication.getAuthorities().toString();

                    if (roles.contains("ROLE_HOD")) {
                        response.sendRedirect("/hod.html");
                    } else if (roles.contains("ROLE_GATE")) {
                        response.sendRedirect("/gate.html");
                    } else {
                        response.sendRedirect("/faculty.html");
                    }
                })

                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html?logout=true")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
