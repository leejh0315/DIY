package project.DIY.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
import project.DIY.form.PasswordUpdateForm;
import project.DIY.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class PasswordUpdateService {
	@Autowired
	private final MemberRepository memberRepository;
	@Autowired
	private final PasswordEncoder passwordEncoder;
	
	public void PasswordUpdate(PasswordUpdateForm passwordUpdateForm, String loginId) {
		
		
	}
	public void validatePasswordUpdateForm(PasswordUpdateForm passwordUpdateForm, Errors errors, String originPasswordCheck, String loginId) {
		
		Member member = memberRepository.selectById(loginId);		
		System.out.println("originPasswordCheck : " + originPasswordCheck);
		
	    if (!StringUtils.hasText(passwordUpdateForm.getOriginPassword())) {
	        errors.rejectValue("originPassword", null, "비밀번호를 입력해주세요.");
	        //if(passwordEncoder.matches(password, member.getPassword())) {
	    } else if (!passwordEncoder.matches(originPasswordCheck, member.getPassword())) {
	        errors.rejectValue("originPassword", null, "비밀번호가 일치하지 않습니다.");
	    }
	    if (!StringUtils.hasText(passwordUpdateForm.getNewPassword())) {
	        errors.rejectValue("newPassword", null, "새 비밀번호를 입력해주세요.");
	    } else if (passwordUpdateForm.getNewPassword().length() < 8) {
	        errors.rejectValue("newPassword", null, "비밀번호는 8자 이상으로 입력해주세요.");
	    } else if(passwordUpdateForm.getNewPassword().equals(passwordUpdateForm.getOriginPassword())) {
	    	errors.rejectValue("newPassword", null, "새 비밀번호는 기존 비밀번호와 다르게 설정해주세요.");
	    } else if (!passwordUpdateForm.getNewPassword().matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}$")) {
	        errors.rejectValue("newPassword", null, "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용해야 합니다.");
	    }
	    if (!StringUtils.hasText(passwordUpdateForm.getNewPasswordCheck())) {
	        errors.rejectValue("newPasswordCheck", null, "비밀번호를 확인 해주세요.");
	    } else if (!passwordUpdateForm.getNewPasswordCheck().equals(passwordUpdateForm.getNewPassword())) {
	        errors.rejectValue("newPasswordCheck", null, "비밀번호가 일치하지 않습니다.");
	    } else if (passwordUpdateForm.getNewPasswordCheck().length() < 8) {
	        errors.rejectValue("newPasswordCheck", null, "비밀번호는 8자 이상으로 입력해주세요.");
	    }

	    
		
	}
	
	public String getSalt() {
		SecureRandom r = new SecureRandom();
		byte[] salt = new byte[20];
		r.nextBytes(salt);
		
		StringBuffer sb = new StringBuffer();
		for(byte b : salt) {
			sb.append((String.format("%02x", b)));
		}
		
		return sb.toString();
	}
	
	public String getEncrypt(String pwd, String salt) {
		String result = "";
		try {
			//1. SHA256 알고리즘 객체 생성
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			//2. pwd와 salt 합친 문자열에 SHA 256 적용
			
			md.update((pwd+salt).getBytes());
			byte[] pwdsalt = md.digest();
			
			//3. byte To String (10진수의 문자열로 변경)
			StringBuffer sb = new StringBuffer();
			for (byte b : pwdsalt) {
				sb.append(String.format("%02x", b));
			}
			
			result=sb.toString();
			
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return result;
		
	}
}
