package fr.hackaton.backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    
    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();

            String userId = jwt.getClaim("userid");
            System.err.println(userId);

            if (userId != null) {
                return userId;
            } else {
                throw new IllegalStateException("User ID not found in JWT token");
            }
        }
        throw new IllegalStateException("No authenticated user found");
    }
}