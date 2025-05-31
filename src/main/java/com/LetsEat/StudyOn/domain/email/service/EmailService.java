package com.LetsEat.StudyOn.domain.email.service;

import com.LetsEat.StudyOn.common.exception.CustomException;
import com.LetsEat.StudyOn.common.exception.ErrorType;
import com.LetsEat.StudyOn.common.util.RandomUtil;
import com.LetsEat.StudyOn.domain.email.dto.EmailVerifyDto;
import com.LetsEat.StudyOn.domain.email.entity.Email;
import com.LetsEat.StudyOn.domain.email.repository.EmailRepository;
import com.LetsEat.StudyOn.domain.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailRepository emailRepository;
    private final UserRepository userRepository;

    /**
     * 인증 메일 발송
     * @param email 메일 전송할 이메일
     */
    @Transactional
    public void sendEmail(String email) {
        // 이미 가입된 이메일인 경우
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorType.DUPLICATED_EMAIL);
        }

        // 인증코드 생성
        String code = RandomUtil.createCode();

        try {
            //메일전송에 필요한 정보 설정
            MimeMessage emailForm = createEmailForm(email, code);
            // 인증 메일 발송
            mailSender.send(emailForm);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new CustomException(ErrorType.MAIL_SEND_PROCESS_ERROR);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new CustomException(ErrorType.UNSUPPORTED_ENCODING);
        }


        // 인증 메일 전송 이력이 있으면
        if (emailRepository.existsByEmail(email)) {
            Email mail = emailRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_MAIL));

            // 인증코드 업데이트
            mail.updateCode(code);
            emailRepository.save(mail);
        }else {
            // 메일 전송 정보 저장(이메일, 인증여부)
            emailRepository.save(Email.of(email, code));
        }
    }

    /**
     * 이메일 인증
     * @param emailVerifyDto 이메일, 인증코드
     */
    @Transactional
    public void verifyEmail(EmailVerifyDto emailVerifyDto) {
        Email email = emailRepository.findByEmail(emailVerifyDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_MAIL));

        // 인증코드 확인 및 인증상태 변경
        email.emailAuth(emailVerifyDto.getCode());
        emailRepository.save(email);
    }

    /**
     * 새비밀번호 메일 발송
     * @param email 이메일
     * @param password 새로운 비밀번호
     */
    public void sendNewPasswordMail(String email, String password) {

        try {
            // 메일 폼 작성
            MimeMessage emailForm = createNewPasswordMailForm(email, password);
            // 메일 발송
            mailSender.send(emailForm);
        }catch (MessagingException e) {
            e.printStackTrace();
            throw new CustomException(ErrorType.MAIL_SEND_PROCESS_ERROR);
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new CustomException(ErrorType.UNSUPPORTED_ENCODING);
        }
    }

    // 새비밀번호 메일 양식
    public MimeMessage createNewPasswordMailForm(String email, String password) throws MessagingException, UnsupportedEncodingException {
        String setFrom = "test@gmail.com";
        String title = "새비밀번호";

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject(title);

        // 메일 내용
        String msgOfEmail="";
        msgOfEmail += "<div style='margin:20px;text-align:center;width: 495px;margin: 0 auto;'>";
        msgOfEmail += "<h1> 안녕하세요 StudyOn 입니다. </h1>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>아래 비밀번호를 통해 로그인해주세요.<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>감사합니다.<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<div align='center' style='border:1px solid black;font-family:verdana;border-radius: 30px;'>";
        msgOfEmail += "<div style='font-size:130%; margin-top: 20px;'>";
        msgOfEmail += "PASSWORD : <strong>";
        msgOfEmail += password + "</strong><div><br/> ";
        msgOfEmail += "</div>";

        message.setFrom(setFrom);
        message.setText(msgOfEmail, "utf-8", "html");

        return message;
    }

    // 인증코드 메일 양식
    public MimeMessage createEmailForm(String email, String code) throws MessagingException, UnsupportedEncodingException {
        String setFrom = "test@gmail.com";
        String title = "인증번호";

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject(title);

        // 메일 내용
        String msgOfEmail="";
        msgOfEmail += "<div style='margin:20px;text-align:center;width: 495px;margin: 0 auto;'>";
        msgOfEmail += "<img src='https://ifh.cc/g/VLQ01c.png' style='border-radius: 60px;'>";
        msgOfEmail += "<h1> 안녕하세요 StudyOn 입니다. </h1>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>아래 코드를 입력해주세요<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>감사합니다.<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<div align='center' style='border:1px solid black;font-family:verdana;border-radius: 30px;'>";
        msgOfEmail += "<div style='font-size:130%; margin-top: 20px;'>";
        msgOfEmail += "CODE : <strong>";
        msgOfEmail += code + "</strong><div><br/> ";
        msgOfEmail += "</div>";

        message.setFrom(setFrom);
        message.setText(msgOfEmail, "utf-8", "html");

        return message;
    }
}
