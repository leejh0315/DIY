package project.DIY.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
import project.DIY.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class LoginService {
	
	private final MemberRepository memberRepository;
	@Autowired
	private final PasswordEncoder passwordEncoder;
	
	public Member login(String id, String password) {
		Member member = memberRepository.selectById(id);
		if(member != null) {
			if(passwordEncoder.matches(password, member.getPassword())) {
				System.out.println("로그인 성공");
				return member;
			}
		}
		return null;
	}
	
	//1225
	public Member passwordCheck(String email, String password) {
		
		Member memberVO = memberRepository.selectById(email);
		
		if(memberVO != null) {
			if(memberVO.getPassword().equals(password)) {
				return memberVO;
			}
		}
		return null;
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