package project.DIY.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
import project.DIY.domain.Post;
import project.DIY.form.JoinForm;
import project.DIY.form.LoginForm;
import project.DIY.repository.MemberRepository;
import project.DIY.repository.PostRepository;
import project.DIY.service.LoginService;
import project.DIY.service.RedisUtils;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {
   
   @Autowired
   private final MemberRepository memberRepository;
   @Autowired
   private final PostRepository postRepository;
   @Autowired
   private final LoginService loginService;
   @Autowired
   private final RedisUtils redisUtils;
   
   private String userTempEmail;
   @Autowired
   private PasswordEncoder passwordEncoder;
   
   @GetMapping("/home")
   public String getMain(Model model, HttpServletRequest req) {
      HttpSession session = req.getSession();
      Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
      
      LocalDate now = LocalDate.now();
      int monthValue = now.getMonthValue();
      
      List<Map<String,String>> king = memberRepository.thisMonthWriteKing(monthValue);
      List<Post> post = postRepository.selectByPostCtCodeHome("book");
      
      
      
      model.addAttribute("member", member);
      model.addAttribute("king", king);
      model.addAttribute("post", post);

      return "main/main";
   }
   
   @PostMapping("/{type}")
   @ResponseBody
   public List<Post> mainTypeResp(@PathVariable("type") String type, Model model) {
      
      if(type.equals("movie"))    {
    	  List<Post> l =postRepository.selectByPostCtCodeHome(type);
    	  for(int i = 0 ; i < l.size(); i++) {
    		  System.out.println(l.get(i).getTargetName());
    	  }
    	  System.out.println("------------------------------------");
    	  return postRepository.selectByPostCtCodeHome(type);
	  }
      else if(type.equals("book")){
    	  List<Post> l =postRepository.selectByPostCtCodeHome(type);
    	  for(int i = 0 ; i < l.size(); i++) {
    		  System.out.println(l.get(i).getTargetName());
    	  }
    	  System.out.println("------------------------------------");
    	  return postRepository.selectByPostCtCodeHome(type);
	  }
      else{
    	  List<Post> l =postRepository.selectByPostCtCodeHome(type);
    	  for(int i = 0 ; i < l.size(); i++) {
    		  System.out.println(l.get(i).getTargetName());
    	  }
    	  System.out.println("------------------------------------");
    	  return postRepository.selectByPostCtCodeHome(type);
	  }

   }
   
   
   @GetMapping("/join")
   public String getJoin(Model model) {

      JoinForm joinForm = new JoinForm();
      model.addAttribute("joinForm", joinForm);
      
      return "join/join";
   }
   
   @PostMapping("/tempEmailSave")
   public void tempEmailSave(@RequestParam(value = "email") String email) {
	   userTempEmail = email;
   }
   
   
   @GetMapping("/join2")
   public String getJoin2(Model model) {
	  JoinForm joinForm = new JoinForm();
      model.addAttribute("joinForm", joinForm);
      if(userTempEmail == null) {
    	  System.out.println(userTempEmail);
    	  return "join/join";
      }else if(!redisUtils.getData(userTempEmail).isEmpty()) {
    	  System.out.println(userTempEmail);
    	  model.addAttribute("joinForm", joinForm);
    	  return "join/join2";
      }else {
    	  return "join/join";
      }
      
   }

   
   @PostMapping("/join2")
   public String postJoin(@ModelAttribute JoinForm joinForm, Model model, BindingResult bindingResult){
      System.out.println(joinForm);
      validateJoinForm(joinForm, bindingResult);
      if(bindingResult.hasErrors()) {
    	  model.addAttribute("joinForm", joinForm);
         return "join/join2";
      }else if(!redisUtils.getData(joinForm.getLoginId()).isEmpty() &&
    		  (!redisUtils.getData(joinForm.getLoginId()).equals("Y") || redisUtils.getData(joinForm.getLoginId())=="")) {
    	  model.addAttribute("joinForm", joinForm);
         return "join/join2";
      }
      else {
         Member member = new Member();
         member.setLoginId(joinForm.getLoginId());
         member.setPassword(passwordEncoder.encode(joinForm.getPassword()));
         member.setNickName(joinForm.getNickName());
         memberRepository.insertMember(member);
         return "redirect:/" + "home/home";   
      }
      
   }
   
   @GetMapping("/dologin")
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
         , HttpServletRequest req, Model model
         , @RequestParam(name="redirectURL", defaultValue = "/") String redirectURL ) {

      System.out.println(loginForm);
      validateLoginForm(loginForm, bindingResult);
      Member memberVO = loginService.login(loginForm.getLoginId(), loginForm.getPassWord());
      
      if(bindingResult.hasErrors()) {
    	  model.addAttribute("loginForm", loginForm);
         return "login/login";
      }
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
      
      HttpSession session = req.getSession(true);
      session.setAttribute(SessionVar.LOGIN_MEMBER, memberVO);

      memberVO.setActiveUUID(session.getId());
      memberRepository.updateUUID(memberVO);
      
      List<Member> member = memberRepository.passwordUpdateSixMonth();
      for(int i = 0 ; i < member.size(); i++) {
    	  if(member.get(i).getLoginId().equals(memberVO.getLoginId())) {
    		  return "redirect:/myPage/passwordUpdate/" + memberVO.getId();
    	  }
      }
      return "redirect:/" + "home/home";
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
      return "redirect:/" + "home/home";
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
	    if (!StringUtils.hasText(joinForm.getLoginId())) {
	        errors.rejectValue("loginId", null, "아이디 필수 입력입니다.");
	    } else if (!joinForm.getLoginId().matches("^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$")) {
	        errors.rejectValue("loginId", null, "올바른 이메일 형식이 아닙니다.");
	    }

	    if (!StringUtils.hasText(joinForm.getPassword())) {
	        errors.rejectValue("password", null, "비밀번호 필수 입력입니다.");
	    } else if (joinForm.getPassword().length() < 8) {
	        errors.rejectValue("password", null, "비밀번호는 8자 이상으로 입력해주세요.");
	    } else if (!joinForm.getPassword().matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}$")) {
	        errors.rejectValue("password", null, "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용해야 합니다.");
	    }

	    if (!StringUtils.hasText(joinForm.getPasswordCheck())) {
	        errors.rejectValue("passwordCheck", null, "비밀번호 확인을 해주세요.");
	    } else if (!joinForm.getPassword().equals(joinForm.getPasswordCheck())) {
	        errors.rejectValue("passwordCheck", null, "비밀번호가 일치하지 않습니다.");
	    } else if (joinForm.getPassword().length() < 8) {
	        errors.rejectValue("passwordCheck", null, "비밀번호는 8자 이상으로 입력해주세요.");
	    }

	    if (!StringUtils.hasText(joinForm.getNickName())) {
	        errors.rejectValue("nickName", null, "닉네임을 입력해주세요.");
	    } else if (!joinForm.getNickName().matches("^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$")) {
	        errors.rejectValue("nickName", null, "닉네임은 특수문자를 제외한 2~10자리여야 합니다.");
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