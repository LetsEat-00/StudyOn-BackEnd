package com.LetsEat.StudyOn.domain.user.service;

import com.LetsEat.StudyOn.common.exception.CustomException;
import com.LetsEat.StudyOn.common.exception.ErrorType;
import com.LetsEat.StudyOn.common.security.jwt.TokenProvider;
import com.LetsEat.StudyOn.common.util.RandomUtil;
import com.LetsEat.StudyOn.domain.email.service.EmailService;
import com.LetsEat.StudyOn.domain.user.dto.*;
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
    private final EmailService emailService;

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

    /**
     * 사용자 비밀번호 변경
     * @param passwordDto 현재, 새로운 비밀번호
     * @param user 사용자 정보
     */
    @Transactional
    public void updatePassword(PasswordUpdateDto passwordDto, User user) {
        // 기존 패스워드와 일치하는지 확인
        if(!passwordEncoder.matches(passwordDto.getCurrentPassword(), user.getPassword())){
            throw new CustomException(ErrorType.INVALID_PASSWORD);
        }

        // 새로운 패스워드가 기존 패스워드와 같은지 확인
        if (passwordEncoder.matches(passwordDto.getNewPassword(), user.getPassword())) {
            throw new CustomException(ErrorType.DUPLICATE_PASSWORD);
        }

        user.updatePassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);
    }

    /**
     * 비밀번호 찾기 (새비밀번호 메일로 발송)
     * @param dto 이메일, 이름
     */
    @Transactional
    public void findPassword(PasswordFindDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));

        // 입력한 이름과 동일한지 확인
        if (!user.checkName(dto.getName())) {
            throw new CustomException(ErrorType.INVALID_NAME);
        }

        // 랜덤한 비밀번호 생성
        String newPassword = RandomUtil.generatePassword();

        // 새비밀번호 메일 발송
        emailService.sendNewPasswordMail(dto.getEmail(), newPassword);

        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
