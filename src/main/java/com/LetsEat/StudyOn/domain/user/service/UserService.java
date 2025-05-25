package com.LetsEat.StudyOn.domain.user.service;

import com.LetsEat.StudyOn.common.exception.CustomException;
import com.LetsEat.StudyOn.common.exception.ErrorType;
import com.LetsEat.StudyOn.common.security.jwt.TokenProvider;
import com.LetsEat.StudyOn.domain.user.dto.SignupRequestDto;
import com.LetsEat.StudyOn.domain.user.dto.TokenRequestDto;
import com.LetsEat.StudyOn.domain.user.dto.TokenResponseDto;
import com.LetsEat.StudyOn.domain.user.entity.User;
import com.LetsEat.StudyOn.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    /**
     * 회원가입
     * @param dto 회원가입 시 필요한 정보
     * @return 회원가입된 사용자 엔티티
     */
    @Transactional
    public User signup(SignupRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new CustomException(ErrorType.DUPLICATED_EMAIL);
        }
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        return userRepository.save(User.of(dto, encodedPassword));
    }

    /**
     * Access/Refresh 토큰 재발급
     * @param tokenRequestDto Access/Refresh 토큰 정보
     * @return 재발급된 Access/Refresh 또는 Access 토큰 정보
     */
    @Transactional
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto) {
        // Refresh Token 검증
        if (tokenProvider.refreshTokenPeriodCheck(tokenRequestDto.getRefreshToken())) {
            throw new CustomException(ErrorType.INVALID_REFRESH_TOKEN);
        }

        // AccessToken 에서 사용자 정보 가져오기(email)
        Claims info = tokenProvider.getUserInfoFromToken(tokenRequestDto.getAccessToken()
                .substring(TokenProvider.BEARER_PREFIX.length()));

        // email 에 해당하는 User 의 refreshToken 을 가져옴
        User user = userRepository.findByEmail(info.getSubject())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));

        // refreshToken 이 일치하는지 검사
        if (!user.getRefreshToken().equals(tokenRequestDto.getRefreshToken()
                .substring(TokenProvider.BEARER_PREFIX.length()))) {
            throw new CustomException(ErrorType.INVALID_USER_INFO);
        }

        // 새로운 토큰 생성
        TokenResponseDto tokenResponseDto = null;
        if (tokenProvider.refreshTokenPeriodCheck(tokenRequestDto.getRefreshToken())) {
            // Refresh Token의 유효기간이 3일 미만일 경우 전체(Access / Refresh) 재발급
            String newAccessToken = tokenProvider.createAccessToken(user.getEmail(), user.getUserRole());
            String newRefreshToken = tokenProvider.createRefreshToken(user.getEmail(), user.getUserRole());

            tokenResponseDto = new TokenResponseDto(newAccessToken, newRefreshToken);

            // RefreshToken 업데이트
            user.setRefreshToken(newRefreshToken);
            userRepository.save(user);
        } else {
            // RefreshToken 의 유효기간이 3일 이상일 경우 AccessToken 만 재발급
            String newAccessToken = tokenProvider.createAccessToken(user.getEmail(), user.getUserRole());
            long now = (new Date()).getTime();
            Date accessTokenExpiresIn = new Date(now + TokenProvider.ACCESS_TOKEN_EXPIRE_TIME);
            tokenResponseDto = new TokenResponseDto(newAccessToken, accessTokenExpiresIn.getTime());
        }

        return tokenResponseDto;
    }

}
