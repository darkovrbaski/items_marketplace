package me.darkovrbaski.items.marketplace.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class JwtUtil {

  static final Integer EXPIRES_IN = 1000 * 60 * 60 * 24;
  static final String ISSUER = "com.darkovrbaski.items.marketplace";

  @Value("${jwt.secret}")
  String jwtSecret;

  public String extractUsername(final String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(final UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  public String generateToken(final Map<String, Object> extraClaims,
      final UserDetails userDetails) {
    return Jwts.builder()
        .setIssuer(ISSUER)
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRES_IN))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean isTokenValid(final String token, final UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  public Date extractExpiration(final String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Boolean isTokenExpired(final String token) {
    return extractExpiration(token).before(new Date());
  }

  private Claims extractAllClaims(final String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSigningKey() {
    final byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

}
