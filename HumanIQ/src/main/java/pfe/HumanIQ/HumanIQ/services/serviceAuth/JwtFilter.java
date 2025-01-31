package pfe.HumanIQ.HumanIQ.services.serviceAuth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pfe.HumanIQ.HumanIQ.services.serviceUser.UserService;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;

    public JwtFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String userName = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                userName = jwtService.extractUserName(token);
                System.out.println("Token extrait: " + token);
                System.out.println("Nom d'utilisateur extrait: " + userName);
                System.out.println("Validation du jeton pour l'utilisateur: " + userName);
                UserDetails userDetails = userService.loadUserByUsername(userName);
                if (userDetails != null) {
                    System.out.println("Utilisateur trouvé: " + userDetails.getUsername());
                    System.out.println("Détails de l'utilisateur : " + userDetails);
                    if (jwtService.validateToken(token, userDetails)) {
                        System.out.println("Authentification réussie pour l'utilisateur: " + userName);
                        System.out.println("Autorités de l'utilisateur : " + userDetails.getAuthorities());
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        System.out.println("Jeton invalide pour l'utilisateur: " + userName);
                        System.out.println("Raison de l'échec de validation : " + jwtService.getValidationError(token, userDetails));
                        if (jwtService.getValidationError(token, userDetails).contains("expired")) {
                            System.out.println("Le jeton a expiré");
                        } else if (jwtService.getValidationError(token, userDetails).contains("invalid")) {
                            System.out.println("Le jeton est invalide");
                        } else {
                            System.out.println("Erreur inconnue lors de la validation du jeton");
                        }
                    }
                } else {
                    System.out.println("Aucun utilisateur trouvé avec le nom: " + userName);
                }
            } else {
                System.out.println("Aucun jeton trouvé dans l'en-tête Authorization");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'authentification: " + e.getMessage());
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}
