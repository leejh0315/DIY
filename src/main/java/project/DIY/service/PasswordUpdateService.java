package project.DIY.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
import project.DIY.domain.PasswordHistory;
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
		PasswordHistory pH = new PasswordHistory();
		List<PasswordHistory> pHList = memberRepository.passwordHistoryByMemberId(member.getId());
		int count = 0;
		System.out.println(pHList);
		
		for(int i = 0 ; i < pHList.size(); i++) {
			if(passwordEncoder.matches(passwordUpdateForm.getNewPassword(), pHList.get(i).getPassword())) {
				System.out.println("일치");
				count ++;
			}
		}
		System.out.println("originPasswordCheck : " + originPasswordCheck);
		System.out.println("count : " + count);
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
	    } else if (count > 0) {
    	  errors.rejectValue("newPassword", null, "이전에 사용한 비밀번호입니다. 다른 비밀번호로 설정해 주세요.");
	    }
	    
	    
	    if (!StringUtils.hasText(passwordUpdateForm.getNewPasswordCheck())) {
	        errors.rejectValue("newPasswordCheck", null, "비밀번호를 확인 해주세요.");
	    } else if (!passwordUpdateForm.getNewPasswordCheck().equals(passwordUpdateForm.getNewPassword())) {
	        errors.rejectValue("newPasswordCheck", null, "비밀번호가 일치하지 않습니다.");
	    } else if (passwordUpdateForm.getNewPasswordCheck().length() < 8) {
	        errors.rejectValue("newPasswordCheck", null, "비밀번호는 8자 이상으로 입력해주세요.");
	    }
	}

}
