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
import project.DIY.service.RedisUtils;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor
public class HomeController {
	
	@Autowired
	private final MemberRepository memberRepository;
	private final LoginService loginService;
	private final RedisUtils redisUtils;
	
	@GetMapping("/")
	public String getMain(Model model, HttpServletRequest req) {
		HttpSession session = req.getSession();
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);

		model.addAttribute("member", member);

		return "main/main";
	}
	
	@GetMapping("/join")
	public String getJoin(Model model) {

		JoinForm joinForm = new JoinForm();
		model.addAttribute("joinForm", joinForm);
		
		return "join/join";
	}
	
	@PostMapping("/join")
	public String postJoin(@ModelAttribute JoinForm joinForm,
			BindingResult bindingResult){
		
		System.out.println(joinForm);
		validateJoinForm(joinForm, bindingResult);

		
		if(bindingResult.hasErrors()) {
			return "join/join";
		}else if(!redisUtils.getData(joinForm.getLoginId()).equals("Y") || redisUtils.getData(joinForm.getLoginId())=="") {
			return "join/join";
		}
		else {
			Member member = new Member();
			member.setLoginId(joinForm.getLoginId());
			member.setPassword(joinForm.getPassword());
			member.setNickName(joinForm.getNickName());
			memberRepository.insertMember(member);
			return "redirect:/";	
		}
		
	}
	
	@GetMapping("/login")
	public String login(Model model, HttpServletRequest req) {
		LoginForm loginForm = new LoginForm();
		
		HttpSession session = req.getSession();
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);

		model.addAttribute("member", member);
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
			bindingResult.reject("loginForm", "아이디 또는 비밀번호를 잘못 입력했습니다.");
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
		return "redirect:/";
	}
	
	@PostMapping("/logout")
	public String logout(HttpServletResponse resp, HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		if(session != null) {
			Member memberVO = (Member)session.getAttribute(SessionVar.LOGIN_MEMBER);
			memberVO.setActiveUUID(null);
			memberRepository.updateUUID(memberVO);
			session.invalidate();
		}
		return "redirect:/";
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
		}else if(joinForm.getPassword().length() < 8) {
			errors.rejectValue("password",null, "비밀번호는 8자 이상으로 입력해주세요.");
		}
		
		if(!StringUtils.hasText(joinForm.getPasswordCheck())){
			errors.rejectValue("passwordCheck", null, "비밀번호 확인을 해주세요.");
		}else if(!joinForm.getPassword().equals(joinForm.getPasswordCheck())) {
			errors.rejectValue("passwordCheck", null,"비밀번호가 올바르지 않습니다.");
		}else if(joinForm.getPassword().length() < 8) {
			errors.rejectValue("passwordCheck", null,"비밀번호는 8자 이상으로 입력해주세요.");
		}
		
		if(!StringUtils.hasText(joinForm.getNickName())){
			errors.rejectValue("nickName", null,"닉네임을 입력해주세요.");
		}
	}
	
	@ResponseBody
	@PostMapping("/joongbok")
	public int idCheck(@RequestParam("email") String email) {
		System.out.println("중복체크 진입");
		System.out.println("email:" + email);
		int cnt = memberRepository.idCheck(email);
		System.out.println("cnt : " + cnt);
		return cnt;
	}
	
}
