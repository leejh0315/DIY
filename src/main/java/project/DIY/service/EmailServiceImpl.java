package project.DIY.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.Message.RecipientType;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

 
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
 
    @Autowired
    JavaMailSender emailSender;
    
    private final RedisUtils redisUtils;
    
    public static String ePw = createKey();
 
    private MimeMessage createMessage(String to)throws Exception{
    	ePw = createKey();
    	redisUtils.setDataExpire(to, ePw, 60*3L);//3분 유효시간
    	System.out.println("보내는 대상 : "+ to);
        System.out.println("인증 번호 : "+ePw);
        RedisProperties r = new RedisProperties();
        MimeMessage  message = emailSender.createMimeMessage();
        
        message.addRecipients(RecipientType.TO, to);//보내는 대상
        message.setSubject("이메일 인증");//제목
 
        String msgg="";
        msgg+= "<div style='margin:20px;'>";
        msgg+= "<h1> 안녕하세요 DIY입니다. </h1>";
        msgg+= "<br>";
        msgg+= "<p>아래의 인증번호를 입력해주세요.<p>";
        msgg+= "<br>";
        msgg+= "<p>감사합니다.<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>회원가입 인증번호 입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+= ePw+"</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("DIY@gmail.com","<DIY>"));//보내는 사람
 
        return message;
    }
    
    private MimeMessage createMessagePassword(String to)throws Exception{
    	ePw = createKey();
        MimeMessage  message = emailSender.createMimeMessage();
        
        message.addRecipients(RecipientType.TO, to);//보내는 대상
        message.setSubject("비밀번호 발송");//제목
 
        String msgg="";
        msgg+= "<div style='margin:20px;'>";
        msgg+= "<h1> 안녕하세요 DIY입니다.. </h1>";
        msgg+= "<br>";
        msgg+= "<p>감사합니다.<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>회원님의 임시 비밀번호입니다.</h3>";
        msgg+= "<h6 style='color:blue;'>로그인 이후, 비밀번호를 꼭 수정하여 주십시오.</h6>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "PASSWORD :" + ePw + "<strong>";
        msgg+= "</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("humanedu6.4@gmail.com","<DIY>"));//보내는 사람
        
 
        return message;
    }
 
    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random(System.currentTimeMillis());
 
        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤
 
            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }
    
    
    @Override
    public String sendSimpleMessage(String to)throws Exception {
        // TODO Auto-generated method stub
        MimeMessage message = createMessage(to);
        try{//예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw;
    }
    
    @Override
    public String sendSimpleMessagePassword(String to)throws Exception {
        // TODO Auto-generated method stub
        MimeMessage message = createMessagePassword(to);
        try{//예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw;
    }
    
    
}
