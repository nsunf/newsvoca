package com.nsunf.newsvoca.config;

import com.nsunf.newsvoca.service.MemberService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class TokenProvider {
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 1000L;
    private final Key key;

    private final MemberService memberService;

    @Autowired
    public TokenProvider(@Value("${jwt.secret}") String secret, MemberService memberService) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.memberService = memberService;
    }

    public String createToken(String userPK, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPK);
        claims.put("roles", roles);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = memberService.loadUserByUsername(this.getUserPK(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserPK(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        System.out.println("bearer token : " + bearerToken);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);
        else
            return null;
    }

    public boolean validateToken(String token) {
        try {
//            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//            return !claims.getBody().getExpiration().before(new Date());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            System.err.println("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            System.err.println("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            System.err.println("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            System.err.println("JWT 토큰이 잘못되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}