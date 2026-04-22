package ntt.beca.films.auth;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import ntt.beca.films.user.UserEntity;

@Service
public class JwtService {

      @Value("${security.jwt.expiration.minutes}")
      private Long expirationMinutes;

      @Value("${security.jwt.secret.key}")
      private String secretKey;

      private SecretKey key;

      @PostConstruct
      public void init() {
            key = Keys.hmacShaKeyFor(secretKey.getBytes());
      }

      public String generateToken(UserEntity user, Map<String, Object> extraClaims) {
            Date issueAt = new Date(System.currentTimeMillis());
            Date expiration = new Date(issueAt.getTime() + (expirationMinutes * 60 * 1000));
            return Jwts.builder()
                        .claims(extraClaims)
                        .subject(user.getEmail())
                        .issuedAt(issueAt)
                        .expiration(expiration)
                        .signWith(key, SIG.HS256)
                        .compact();
      }

      public String extractEmail(String jwt) {
            return Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload()
                        .getSubject();
      }

}
