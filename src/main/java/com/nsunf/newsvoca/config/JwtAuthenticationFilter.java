package com.nsunf.newsvoca.config;

import com.nsunf.newsvoca.service.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {

        // get username, jwtToken from request
        String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenProvider.getUserPK(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            System.out.println("JWT Token does not begin with Bearer String");
        }

        // validation check
        if (username != null
                && SecurityContextHolder.getContext().getAuthentication() == null
                && jwtTokenProvider.validateToken(jwtToken))
        {
            // set authentication
            UserDetails userDetails = this.memberService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    // 1. HttpServletRequest의 Header에서 Authorization(X-AUTH-TOKEN)을 가져온다.
    // 2. (1)의 형식이 Bearer인지 확인후 token 부분을 가져온다.
    // 3. (2)의 token을 이용하여 username을 가져온다.
    // 4. (3)의 username을 이용하여 UserDetails를 가져온다.
    // 5. (1)의 token의 유효성 검사(기한 만기 여부 확인) (userDetails의 username과 Claims의 username을 비교)
    // 6. (4)의 userDetails를 이용하여 usernamePasswordAuthenticationToken을 생성, SecurityContext에 설정한다.
}
