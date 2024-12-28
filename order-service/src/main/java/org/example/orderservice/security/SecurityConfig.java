package org.example.orderservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private JwtAuthConverter jwtAuthConverter;
    public SecurityConfig(JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                //Toutes les requêtes nécessitent une authentification sauf les requete /api/
                .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf->csrf.disable())
                .cors(Customizer.withDefaults())
                //autoriser les frams
                .headers(h->h.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
                .authorizeHttpRequests(ar->ar.requestMatchers("/h2-console/**","/swagger-ui.html","/swagger-ui/**","/v3/**","/api-docs").permitAll())
                //.authorizeHttpRequests(ar->ar.requestMatchers("/h2-console/**","/api/orders/**","/swagger-ui.html","/swagger-ui/**","/v3/**","/api-docs").permitAll())
                //.authorizeHttpRequests(ar->ar.requestMatchers("/api/products/**").hasAuthority("ADMIN"))
                .authorizeHttpRequests(ar->ar.anyRequest().authenticated())
                // .oauth2ResourceServer(o2 -> o2.jwt(Customizer.withDefaults())) //1:43 cherche les scopes //Spring Security : à chaque fois qu'il y a une requête avec un header Authorization, il va trouver le JWT. Ce JWT est vérifié pour savoir s'il a expiré et si la signature du token est correcte via la clé publique (qui peut être insérée dans les propriétés de l'application ou récupérée via Keycloak). À partir de ce token, on peut obtenir les rôles, le nom d'utilisateur, etc., et sécuriser le contexte
               .oauth2ResourceServer(o2 -> o2.jwt(jwt->jwt.jwtAuthenticationConverter(jwtAuthConverter)))
                .build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200")); // Allow requests from your Angular app
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // Allow necessary HTTP methods
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // Allow specific headers
        corsConfiguration.setAllowCredentials(true); // If you need to send cookies or credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }


}
