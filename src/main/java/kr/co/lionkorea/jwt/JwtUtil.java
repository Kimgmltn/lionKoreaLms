package kr.co.lionkorea.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import kr.co.lionkorea.dto.CustomUserDetails;
import kr.co.lionkorea.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
//    private final Long JWT_EXPIRATION_MS_ACCESS = 60*5*1000L; // 1000ms * 60(1분) * 5 = 5분
    private final Long JWT_EXPIRATION_MS_ACCESS = 60*1000L; // 1000ms * 60(1분) = 1분
    private final Long JWT_EXPIRATION_MS_REFRESH = 60*60*8*1000L; // 1000ms * 60(1분) * 60 * 8 = 8시간

    public JwtUtil(@Value("${spring.jwt.secret}") String secret){
//        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getLoginId(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("loginId", String.class);
    }

    public String getMemberName(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("memberName", String.class);
    }

    public Long getMemberId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("memberId", Long.class);
    }

    public Set<Role> getRoles(String token){
        List<String> roles = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("roles", List.class);
        return roles.stream().map(Role::valueOf).collect(Collectors.toSet());
    }

    public Boolean isExpire(String token){
        try{
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
        }catch(ExpiredJwtException e){
            return true;
        }
    }

    public String getCategory(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public Long getAccountId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("accountId", Long.class);
    }

    public String createJwt(String category, CustomUserDetails customUserDetails){
        if ("access".equals(category)) {
            return Jwts.builder()
                    .claim("category", category)
                    .claim("memberName", customUserDetails.getUsername())
                    .claim("roles", customUserDetails.getRoles())
                    .claim("memberId", customUserDetails.getMemberId())
                    .claim("accountId", customUserDetails.getAccountId())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS_ACCESS))
                    .signWith(secretKey)
                    .compact();
        }else{
            return Jwts.builder()
                    .claim("category", category)
                    .claim("memberName", customUserDetails.getUsername())
                    .claim("roles", customUserDetails.getRoles())
                    .claim("memberId", customUserDetails.getMemberId())
                    .claim("accountId", customUserDetails.getAccountId())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS_REFRESH)) // refresh token은 8시간
                    .signWith(secretKey)
                    .compact();
        }
    }
}
