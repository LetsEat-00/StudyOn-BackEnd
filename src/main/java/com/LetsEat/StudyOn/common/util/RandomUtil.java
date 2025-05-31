package com.LetsEat.StudyOn.common.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class RandomUtil {
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*?";

    private static final String ALL = UPPER + LOWER + DIGITS + SPECIAL;

    private static final SecureRandom random = new SecureRandom();

    // 랜덤한 비밀번호 생성(최소 10자 ~ 최대 15자)
    public static String generatePassword() {
        int length = 10 + random.nextInt(6); // 10~15자

        List<Character> passwordChars = new ArrayList<>();

        // 최소 1자씩 포함
        passwordChars.add(UPPER.charAt(random.nextInt(UPPER.length())));
        passwordChars.add(LOWER.charAt(random.nextInt(LOWER.length())));
        passwordChars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        passwordChars.add(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        // 나머지 랜덤 문자 채우기
        for (int i = 4; i < length; i++) {
            passwordChars.add(ALL.charAt(random.nextInt(ALL.length())));
        }

        // 셔플해서 랜덤한 위치에 배치
        Collections.shuffle(passwordChars, random);

        // 리스트 -> 문자열 변환
        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        return password.toString();
    }

    // 랜덤 인증코드 8자 생성
    public static String createCode() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for(int i=0; i<8; i++) {
            int idx = random.nextInt(3);

            switch (idx) {
                case 0 :
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(10));
                    break;
            }
        }
        return key.toString();
    }
}
