package project.DIY.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
import project.DIY.form.JoinForm;
import project.DIY.form.LoginForm;
import project.DIY.repository.MemberRepository;
import project.DIY.service.LoginService;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor
public class HomeController {
	
	@Autowired
	private final MemberRepository memberRepository;
	private final LoginService loginService;
	
	@GetMapping("/")
	public String getMain(Model model) {
		
		return "main/main";
	}
	
	@GetMapping("/join")
	public String getJoin() {

		return "join/join";
	}
	
	@PostMapping("/join")
	public String postJoin(@ModelAttribute JoinForm joinForm,
			BindingResult bindingResult, HttpServletResponse resp
			, HttpServletRequest req
			, @RequestParam(name="redirectURL", defaultValue = "/") String redirectURL){
		validateJoinForm(joinForm, bindingResult);
		if(bindingResult.hasErrors()) {
			return "login/login";
		}
		return "";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		LoginForm loginForm = new LoginForm();
		model.addAttribute("loginForm", loginForm);
	
		return "login/login";
	}
	
	@PostMapping("/login")
	public String doLogin(@ModelAttribute LoginForm loginForm,
			BindingResult bindingResult, HttpServletResponse resp
			, HttpServletRequest req
			, @RequestParam(name="redirectURL", defaultValue = "/") String redirectURL ) {
		validateLoginForm(loginForm, bindingResult);
		
		if(bindingResult.hasErrors()) {
			return "login/login";
		}
		Member memberVO = loginService.login(loginForm.getLoginId(), loginForm.getPassWord());
		
		if(memberVO == null) { //계정정보가 없거나, 비밀번호가 안맞거나 로그인 실패
			bindingResult.reject("loginForm", "아이디 or 비밀번호 불일치");
			return "login/login";
		}
		if(memberVO.getStatusCode().equals("N")) { //탈퇴한 회원일때...
			bindingResult.reject("loginForm", "탈퇴한 회원입니다. 회원가입을 다시 진행해주세요.");
			return "login/login";
		}
		if(memberVO.getStatusCode().equals("S")) { //정지당한 회원일때...
			bindingResult.reject("loginForm", "활동정지된 회원입니다. 관리자에게 문의해주세요.");
			return "login/login";
		}
		System.out.println("로그인성공");
		HttpSession session = req.getSession();
		session.setAttribute(SessionVar.LOGIN_MEMBER, memberVO);

		memberVO.setActiveUUID(session.getId());
		memberRepository.updateUUID(memberVO);

//		return "redirect:" + redirectURL; 
		return "/join/join";
	}

	public void validateLoginForm(LoginForm loginForm, Errors errors) {
		if(!StringUtils.hasText(loginForm.getLoginId())) {
			errors.rejectValue("loginId", null, "아이디 필수 입력입니다.");
		}
		if(!StringUtils.hasText(loginForm.getPassWord())) {
			errors.rejectValue("passWord", null, "비밀번호 필수 입력입니다.");
		}
	}
	
	public void validateJoinForm(JoinForm joinForm, Errors errors) {
		if(!StringUtils.hasText(joinForm.getLoginId())) {
			errors.rejectValue("loginId", null, "아이디 필수 입력입니다.");
		}
		if(!StringUtils.hasText(joinForm.getPassword())) {
			errors.rejectValue("password", null, "비밀번호 필수 입력입니다.");
		}
		if(!joinForm.getPassword().equals(joinForm.getPasswordCheck())) {
			errors.rejectValue("passwordCheck", "비밀번호가 올바르지 않습니다.");
		}
	}
	
	@ResponseBody
	@PostMapping("/joongbok")
	public int idCheck(@RequestParam("email") String email) {
		
		int cnt = memberRepository.idCheck(email);
		return cnt;
	}
	
}
