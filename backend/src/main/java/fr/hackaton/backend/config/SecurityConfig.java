package fr.hackaton.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2ResourceServer(oauth2Configurer -> oauth2Configurer
                        .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwt -> {
                            Map<String, Collection<String>> realmAccess = jwt.getClaim("realm_access");
                            Collection<String> roles = realmAccess.get("roles");
                            List<SimpleGrantedAuthority> grantedAuthorities = roles.stream()
                                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
                            grantedAuthorities.forEach(e -> {
                                System.err.println(e.getAuthority());
                            });
                            return new JwtAuthenticationToken(jwt, grantedAuthorities);
                        })))
                .authorizeHttpRequests(auth -> {
                    auth
                            .anyRequest().permitAll();
                            // .requestMatchers(
                            //         "/v3/api-docs",
                            //         "/v3/api-docs/**",
                            //         "/swagger-ui/**",
                            //         "/swagger-ui.html",
                            //         "/swagger-resources/**",
                            //         "/docs",
                            //         "/public",
                            //         "/actuator/**",
                            //         "/api/actuator/**"
                            // ).permitAll()
                            // .requestMatchers("/searchProfiles").hasRole("MANAGER")
                            // .anyRequest().authenticated();
                });

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}