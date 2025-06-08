package com.LetsEat.StudyOn.common.security.jwt;

import com.LetsEat.StudyOn.common.exception.CustomException;
import com.LetsEat.StudyOn.common.exception.ErrorType;
import com.LetsEat.StudyOn.common.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "Jwt 검증 및 인가")
@RequiredArgsConstructor
// OncePerRequestFilter 상속 -> HttpServlet 사용 가능해짐
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    // 허용할 요청 (인가 필터를 건너뜀)
    private final RequestMatcher permitRequestMatcher = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/v1/auth/login", "POST"),
            new AntPathRequestMatcher("/api/v1/auth/signup", "POST"),
            new AntPathRequestMatcher("/api/v1/auth/reissue", "POST"),
            new AntPathRequestMatcher("/api/v1/email/send", "POST"),
            new AntPathRequestMatcher("/api/v1/email/verify", "PATCH"),
            new AntPathRequestMatcher("/api/v1/auth/password", "PUT"),
            new AntPathRequestMatcher("/api/v1/users/profile", "GET")
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 허용된 요철일 경우 인증 필터로 넘김
        if (permitRequestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // header 에서 accessToken 가져오기
        String accessToken = tokenProvider.getAccessTokenFromHeader(request);

        // accessToken 이 null 값인지 체크
        if(StringUtils.hasText(accessToken)){
            // accessToken 검증
            if(tokenProvider.validateToken(accessToken)){
                // accessToken 이 유효할 때
                authenticateWithAccessToken(accessToken);
            }
        }
        else {
            log.info("token is null");
            throw new CustomException(ErrorType.NOT_FOUND_TOKEN);
        }

        filterChain.doFilter(request, response);
    }

    // accessToken 이 유효
    public void authenticateWithAccessToken(String token){
        Claims info = tokenProvider.getUserInfoFromToken(token);

        try {
            setAuthentication(info.getSubject());
        } catch (Exception e){
            log.error(e.getMessage());
            throw new CustomException(ErrorType.NOT_FOUND_AUTHENTICATION_INFO);
        }
    }


    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
