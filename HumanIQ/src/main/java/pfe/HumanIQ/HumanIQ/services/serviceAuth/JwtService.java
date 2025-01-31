package pfe.HumanIQ.HumanIQ.services.serviceAuth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {
    private static final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String Username = extractUserName(token);
        boolean isExpired = isTokenExpired(token);
        System.out.println("Email extrait du jeton: " + Username);
        System.out.println("Le jeton est expiré: " + isExpired);
        return (Username.equals(userDetails.getUsername()) && !isExpired);
    }

    public String getValidationError(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        if (!userName.equals(userDetails.getUsername())) {
            return "Le nom d'utilisateur extrait du jeton ne correspond pas à l'utilisateur.";
        }
        if (isTokenExpired(token)) {
            return "Le jeton est expiré.";
        }
        return "Le jeton est invalide pour une autre raison.";
    }
    // Rafraîchir un token expiré
    public String refreshToken(String token, UserDetails userDetails) {
        if (isTokenExpired(token)) {
            return generateToken(userDetails.getUsername());
        }
        return token;
    }
}
