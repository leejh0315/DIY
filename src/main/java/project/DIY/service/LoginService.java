package project.DIY.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
import project.DIY.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class LoginService {
	
	private final MemberRepository memberRepository;
	
	public Member login(String id, String password) {
		Member member = memberRepository.selectById(id);
		
		if(member != null) {
			if(member.getPassWord().equals(password)) {
				return member;
			}
		}
		return null;
	}
	
	//1225
	public Member passwordCheck(String email, String password) {
		
		Member memberVO = memberRepository.selectById(email);
		
		if(memberVO != null) {
			if(memberVO.getPassWord().equals(password)) {
				return memberVO;
			}
		}
		return null;
	}
}