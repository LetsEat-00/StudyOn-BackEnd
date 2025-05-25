package com.LetsEat.StudyOn.common.security.jwt;

import com.LetsEat.StudyOn.common.dto.CommonResponse;
import com.LetsEat.StudyOn.common.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "security 인증/인가 예외처리 핸들러 필터")
@RequiredArgsConstructor
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            e.printStackTrace(); // 에러 로그 출력
            response.setCharacterEncoding("UTF-8"); // 에러 메시지 응답
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            new ObjectMapper().writeValue(response.getWriter(),
                    CommonResponse.builder()
                            .statusCode(e.getErrorType().getHttpStatus().value())
                            .msg(e.getMessage())
                            .build());
        }
    }
}
