package Veterinaria.Cliente.config;

import Veterinaria.Cliente.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Se deshabilita CSRF para API REST stateless
                .csrf(csrf -> csrf.disable())

                // Stateless: el servidor NO guarda sesión, la identidad viaja en el token
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Reglas de acceso por ruta
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()          // Login público
                        .requestMatchers("/api/publico/**").permitAll()   // Rutas públicas
                        .anyRequest().authenticated()                      // Todo lo demás requiere token
                )

                // Filtro JWT se ejecuta antes del filtro estándar de Spring Security
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
