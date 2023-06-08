package com.nsunf.newsvoca.config;

import com.nsunf.newsvoca.constant.Role;
import com.nsunf.newsvoca.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long TOKEN_VALIDATION_TIME = 60 * 60 * 1000L;
    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String userPK, Role roles) {
        Claims claims = Jwts.claims().setSubject(userPK);
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TOKEN_VALIDATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        Date expirationDate = this.getClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    private <T> T getClaim(String token, Function<Claims, T> func) {
        Claims claims = Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(token).getBody();
        return func.apply(claims);
    }

    public String getToken(HttpServletRequest req) {
        String reqTokenHeader = req.getHeader("Authorization");
        System.out.println("--> " + req.getHeaderNames());

        if (reqTokenHeader != null && reqTokenHeader.startsWith("Bearer "))
            return reqTokenHeader.substring(7);

        System.out.println("JWT does not begin with Bearer String : null");
        return null;
    }

    public String getUsername(String token) {
        try {
            return this.getClaim(token, Claims::getSubject);
        } catch (IllegalArgumentException e) {
            System.err.println("Unable to get JWT Token");
        } catch (ExpiredJwtException e) {
            System.err.println("JWT Token has expired");
        }

        return null;
    }

    public void setAuthentication(HttpServletRequest req, String username) {
        UserDetails userDetails = this.memberService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}