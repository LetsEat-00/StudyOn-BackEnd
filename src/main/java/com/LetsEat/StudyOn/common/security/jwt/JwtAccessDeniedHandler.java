package com.LetsEat.StudyOn.common.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    // 인증이 실패했을 떄 실행된다.
    // ROLE_ADMIN 권한이 있는 사용자가 필요한 요청에 ROLE_USER 권한을 가진 사용자가 접근했을 때 실행된다.
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setCharacterEncoding("utf-8");
        response.sendError(403, "권한이 없습니다.");
    }
}