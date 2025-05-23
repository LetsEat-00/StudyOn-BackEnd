package com.LetsEat.StudyOn.common.security.jwt;

import com.LetsEat.StudyOn.common.enums.UserRole;
import com.LetsEat.StudyOn.common.security.UserDetailsImpl;
import com.LetsEat.StudyOn.common.security.dto.LoginRequestDto;
import com.LetsEat.StudyOn.common.util.ControllerUtil;
import com.LetsEat.StudyOn.domain.user.entity.User;
import com.LetsEat.StudyOn.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j(topic = "로그인 및 JWT 생성")
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final AntPathRequestMatcher loginRequestMatcher =
            new AntPathRequestMatcher("/api/v1/auth/login", "POST");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (!loginRequestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("로그인 시도");
        try {
            //json 형태의 String 데이터를 LoginRequestDto 로 변환
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    requestDto.getEmail(), requestDto.getPassword()
            );

            Authentication authentication = authenticationManager.authenticate(authToken);

            //principal -> userDetail -> userDetailsImpl
            User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

            //토큰에 넣을 Email, role 추출
            String email = user.getEmail();
            UserRole role = user.getUserRole();

            //accessToken, refreshToken 생성
            String accessToken = tokenProvider.createAccessToken(email, role);
            String refreshToken = tokenProvider.createRefreshToken(email, role);

            //refreshToken 저장
            user.setRefreshToken(refreshToken.substring(7));
            userRepository.save(user);

            //헤더에 토큰 담기
            response.addHeader(TokenProvider.AUTH_ACCESS_HEADER, accessToken);
            response.addHeader(TokenProvider.AUTH_REFRESH_HEADER, refreshToken);

            // 응답
            log.info("로그인 성공");
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            new ObjectMapper().writeValue(response.getWriter(),
                    Map.of("msg", "로그인 성공", "status", HttpStatus.OK.value()));

        } catch (AuthenticationException ex) {
            // 로그인 실패
            log.info("로그인 실패");
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            new ObjectMapper().writeValue(response.getWriter(),
                    Map.of("msg", "로그인 실패", "status", HttpStatus.UNAUTHORIZED.value()));
        }

        filterChain.doFilter(request, response);
    }
}
