package com.LetsEat.StudyOn.common.security.jwt;

import com.LetsEat.StudyOn.common.enums.UserRole;
import com.LetsEat.StudyOn.common.security.UserDetailsImpl;
import com.LetsEat.StudyOn.common.security.dto.LoginRequestDto;
import com.LetsEat.StudyOn.domain.user.entity.User;
import com.LetsEat.StudyOn.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(TokenProvider tokenProvider, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        setFilterProcessesUrl("/api/v1/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            //json 형태의 String 데이터를 LoginRequestDto로 변환
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error("로그인 시도(attemptAuthentication) 예외 발생 {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    // 로그인 성공시 처리
    @Override
    @Transactional
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        log.info("로그인 성공 및 jwt 생성");

        //principal -> userDetail -> userDetailsImpl
        User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();

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

        //응답
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write("로그인 성공");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.info("로그인 실패");
        response.setStatus(401);
        response.getWriter().write("로그인 실패");
    }
}