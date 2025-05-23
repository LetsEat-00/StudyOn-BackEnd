package com.LetsEat.StudyOn.common.security.jwt;

import com.LetsEat.StudyOn.common.exception.CustomException;
import com.LetsEat.StudyOn.common.exception.ErrorType;
import com.LetsEat.StudyOn.domain.user.entity.User;
import com.LetsEat.StudyOn.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final TokenProvider jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("로그아웃 시도");

        // accessToken, refreshToken 가져오기
        String accessTokenValue = jwtUtil.getAccessTokenFromHeader(request);
        String refreshTokenValue = jwtUtil.getRefreshTokenFromHeader(request);

        // accessToken, refreshToken 값이 없는데 로그아웃을 시도할 경우
        if (accessTokenValue == null && refreshTokenValue == null) {
            log.error("로그아웃 시도 중 에러 발생");
            throw new CustomException(ErrorType.NOT_FOUND_TOKEN);
        }

        // 유저를 찾고, 유저의 refreshToken을 null로 set
        String accountId = jwtUtil.getUserInfoFromToken(refreshTokenValue).getSubject();
        User findUser = userRepository.findByEmail(accountId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));
        findUser.setRefreshToken(null);
        userRepository.save(findUser);

        // SecurityContextHolder 초기화
        SecurityContextHolder.clearContext();
    }
}
