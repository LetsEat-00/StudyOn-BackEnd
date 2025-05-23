package com.LetsEat.StudyOn.common.security.jwt;

import com.LetsEat.StudyOn.common.enums.UserRole;
import com.LetsEat.StudyOn.common.exception.CustomException;
import com.LetsEat.StudyOn.common.exception.ErrorType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j(topic = "TokenProvider")
public class TokenProvider {

    // accessToken 토큰 헤더
    public static final String AUTH_ACCESS_HEADER = "Authorization";
    // refreshToken 토큰 헤더
    public static final String AUTH_REFRESH_HEADER = "RefreshToken";
    // 사용자 권한 키
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // accessToken 만료 시간 (60분)
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 1000L;
    // refreshToken 만료 시간 (2주)
    private final long REFRESH_TOKEN_EXPIRE_TIME = 14 * 24 * 60 * 60 * 1000L;
    // 3일
    private static final long THREE_DAYS = 1000 * 60 * 60 * 24 * 3;


    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init(){
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // AccessToken 생성
    public String createAccessToken(String email, UserRole role){
        Date date = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(email) //사용자 식별값
                .claim(AUTHORIZATION_KEY, role) //사용자 권한
                .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .setIssuedAt(date) //발급일
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // RefreshToken 생성
    public String createRefreshToken(String email, UserRole role){
        Date date = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // header 에서 accessToken 가져오기
    public String getAccessTokenFromHeader(HttpServletRequest request){
        String accessToken = request.getHeader(AUTH_ACCESS_HEADER);
        if(StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER_PREFIX)) {
            return accessToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    // header 에서 RefreshToken 가져오기
    public String getRefreshTokenFromHeader(HttpServletRequest request){
        String accessToken = request.getHeader(AUTH_REFRESH_HEADER);
        if(StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER_PREFIX)) {
            return accessToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    // accessToken 검증
    public boolean validateToken(HttpServletRequest request, String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            throw new CustomException(ErrorType.INVALID_JWT);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
            throw new CustomException(ErrorType.EXPIRED_JWT);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
            throw new CustomException(ErrorType.EXPIRED_JWT);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            throw new CustomException(ErrorType.INVALID_JWT);
        }
    }

    // refreshToken 만료기한 점검
    public boolean refreshTokenPeriodCheck(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token.substring(BEARER_PREFIX.length()));
        long now = (new Date()).getTime();
        long refresh_expiredTime = claimsJws.getBody().getExpiration().getTime();
        long refresh_nowTime = new Date(now + REFRESH_TOKEN_EXPIRE_TIME).getTime();

        // RefreshToken 의 만료기간이 3일 이상일 경우 true
        return refresh_nowTime - refresh_expiredTime > THREE_DAYS;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }
}