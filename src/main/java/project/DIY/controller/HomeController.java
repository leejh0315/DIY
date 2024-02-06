package project.DIY.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import project.DIY.domain.Notice;
import project.DIY.domain.Post;
import project.DIY.form.JoinForm;
import project.DIY.form.LoginForm;
import project.DIY.repository.AboutPostRepository;
import project.DIY.repository.MemberRepository;
import project.DIY.repository.PostRepository;
import project.DIY.service.EmailService;
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
   @Autowired
   private final AboutPostRepository aboutPostRepository;
   
   @Autowired
   private PasswordEncoder passwordEncoder;
   @Autowired
	private final EmailService emailService;
   
   //홈 화면

@GetMapping("/home")
   public String getMain(Model model, HttpServletRequest req) {
      HttpSession session = req.getSession();
      Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
      
      aboutNotice(member, model);
      
      LocalDate now = LocalDate.now();
      int monthValue = now.getMonthValue();
      LocalDate lastMonth = now.minusMonths(1);
      int lastMonthValue = lastMonth.getMonthValue();



            List<Map<String,String>> king = memberRepository.thisMonthWriteKing(monthValue);

      if(king.size() !=3){
      king= memberRepository.thisMonthWriteKing(
      lastMonthValue);

      }
      List<Post> post = postRepository.selectByPostCtCodeHome("book");

      List<String> kingProfileimg =new ArrayList<>();
	      for(int i = 0; i<3;i++) {
	    	  Integer kingid =((Post) king.get(i)).getMemberId();
	    	  
		   	  if(memberRepository.selectBymemberId(kingid).getProfileSrc()==null) {
	    		  kingProfileimg.add("/img/defalut_profileimg.jpg");
	    	  }else { 
	    		  kingProfileimg.add(memberRepository.selectBymemberId(kingid).getProfileSrc());
	    	  }
		    	 
	      }
      
      
      List<String> top5_profileImg = new ArrayList<>();
      List<Post> top5posts = postRepository.selectTop5PopularPosts();
      for(int i=0; i<top5posts.size();i++) {
         String contentTemp = top5posts.get(i).getContent();
         String plainText = contentTemp.replaceAll("\\<.*?\\>", "");;
         plainText = plainText.replaceAll("&nbsp;", "");
         plainText = plainText.replaceAll("&gt;", "");
         int maxLength = 50;
           if (plainText.length() > maxLength) {
               plainText = plainText.substring(0, maxLength);
           }
         top5posts.get(i).setContent(plainText);
         
         int profileurl = top5posts.get(i).getMemberId();
         top5_profileImg.add(memberRepository.selectBymemberId(profileurl).getProfileSrc());
      }
      
      
      model.addAttribute("top5posts" ,top5posts);
      model.addAttribute("top5_profileImg",top5_profileImg);
      model.addAttribute("member", member);
      model.addAttribute("king", king);
      model.addAttribute("post", post);
      model.addAttribute("kingProfileimg",kingProfileimg );

      return "main/main";
   }
   
   
   //홈 화면 토글에 맞는 게시물 반환
   @PostMapping("/{type}")
   @ResponseBody
   public List<Post> mainTypeResp(@PathVariable("type") String type, Model model) {
		
      if(type.equals("movie"))    {
    	  List<Post> post = postRepository.selectByPostCtCodeHome(type);
    	  return post;
	  }else if(type.equals("book")){
    	  List<Post> post = postRepository.selectByPostCtCodeHome(type);
    	  return post;
	  }else{
    	  List<Post> post = postRepository.selectByPostCtCodeHome(type);
    	  return post;
	  }
   }
   
   //회원가입 페이지
   @GetMapping("/join")
   public String getJoin(Model model) {
      JoinForm joinForm = new JoinForm();
      System.out.println(passwordEncoder.encode("1"));
      model.addAttribute("joinForm", joinForm);
      return "join/join";
   }
   
   //회원가입 유효성 검사 이후 DB 저장
   @PostMapping("/join")
   public String postJoin(@ModelAttribute JoinForm joinForm, Model model, BindingResult bindingResult){
      validateJoinForm(joinForm, bindingResult);
      
      if(bindingResult.hasErrors()) {
    	  model.addAttribute("joinForm", joinForm);
         return "join/join";
      }else if(!redisUtils.getData(joinForm.getLoginId()).isEmpty() &&
    		  (!redisUtils.getData(joinForm.getLoginId()).equals("Y") || 
			   redisUtils.getData(joinForm.getLoginId())=="")) {
    	  model.addAttribute("joinForm", joinForm);
         return "join/join";
      }else {
         LocalDateTime currentDateTime = LocalDateTime.now();
         Member member = new Member();
         member.setLoginId(joinForm.getLoginId());
         member.setPassword(passwordEncoder.encode(joinForm.getPassword()));
         member.setNickName(joinForm.getNickName());
         member.setCreateOn(currentDateTime);
         memberRepository.insertMember(member);
         return "redirect:/" + "home/welcome";   
      }
   }
   @GetMapping("/welcome")
   public String welcome(){
	   return "main/welcome";
   }
   
   
   //로그인 페이지
   @GetMapping("/dologin")
   public String login(Model model, HttpServletRequest req) {
      LoginForm loginForm = new LoginForm();
      HttpSession session = req.getSession();
      Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
      model.addAttribute("member", member);
      model.addAttribute("loginForm", loginForm);
      return "login/login";
   }
   
   //로그인 실행, 비밀번호 6개월 이상이거나, 임시비밀번호 유저 비밀번호 페이지로 유도
   @PostMapping("/login")
   public String doLogin(@ModelAttribute LoginForm loginForm,
         BindingResult bindingResult, HttpServletResponse resp , HttpServletRequest req, Model model) {
	  
	   if(redisUtils.getData(loginForm.getLoginId()).equals("LOCK")) {
		   bindingResult.reject("loginForm", "현재 로그인 시도 횟수 초과로 계정이 임시 잠금 처리 되었습니다. 잠시 후에 다시 시도해주세요");
	       return "login/login";
	   }
	   
      validateLoginForm(loginForm, bindingResult);
      Member memberVO = loginService.login(loginForm.getLoginId(), loginForm.getPassWord());
      

      


	  int failCount = 0;
      if(bindingResult.hasErrors()) {
    	  model.addAttribute("loginForm", loginForm);
         return "login/login";
      }
      if(memberVO == null) { //계정정보가 없거나, 비밀번호가 안맞거나 로그인 실패
	     
    	  if(redisUtils.getData(loginForm.getLoginId()) != null && !redisUtils.getData(loginForm.getLoginId()).equals("LOCK")) {
	    	 failCount = Integer.parseInt(redisUtils.getData(loginForm.getLoginId()));
	    	 System.out.println(failCount);
	    	 redisUtils.setData(loginForm.getLoginId(), Integer.toString(failCount+1));
	    	 
    	  } 
	      if(redisUtils.getData(loginForm.getLoginId()) != null &&
	    		  redisUtils.getData(loginForm.getLoginId()).equals("5")){
	     	 bindingResult.reject("loginForm","로그인 실패 5회입니다. 잠시 후에 다시 시도해주세요.");
	     	 System.out.println("로그인 실패 5회입니다. 잠시 후에 다시 시도해주세요.");
	     	 redisUtils.setDataExpire(loginForm.getLoginId(), "LOCK", 60*10L);//10분동안 잠금
	      } 
	      else if(redisUtils.getData(loginForm.getLoginId()) != null &&
				  redisUtils.getData(loginForm.getLoginId()).equals("LOCK")) {
	          bindingResult.reject("loginForm", "현재 로그인 시도 횟수 초과로 계정이 임시 잠금 처리 되었습니다. 잠시 후에 다시 시도해주세요");
	          System.out.println("\"현재 로그인 시도 횟수 초과로 계정이 임시 잠금 처리 되었습니다. 잠시 후에 다시 시도해주세요\"");
	          return "login/login";
	      }
	      else{
	    	  
	      bindingResult.reject("loginForm", "아이디 또는 비밀번호를 잘못 입력했습니다.");
	      }
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
      
      
      redisUtils.setDataExpire(loginForm.getLoginId(), "", 1);
      HttpSession session = req.getSession(true);
      session.setAttribute(SessionVar.LOGIN_MEMBER, memberVO);
      
      
      
      memberVO.setActiveUUID(session.getId());
      memberRepository.updateUUID(memberVO);
      
      List<Member> member = memberRepository.passwordUpdateSixMonth();
      System.out.println(member);
      for(int i = 0 ; i < member.size(); i++) {
    	  if(memberVO.getLoginId().equals(member.get(i).getLoginId())) {
	    	  if(member.get(i).getLoginId().equals(memberVO.getLoginId())) {
	    		  return "redirect:/myPage/passwordUpdate/" + memberVO.getId();
	    	  }
    	  }
      }
      List<Member> allMember = memberRepository.selectAllUser();
      for(int i = 0 ; i<allMember.size(); i++) {
    	  if(memberVO.getLoginId().equals(allMember.get(i).getLoginId())) {
    		  if(allMember.get(i).getPasswordFind().equals("Y")) {
        		  System.out.println("pw찾음");
        		  return "redirect:/myPage/passwordUpdate/" + memberVO.getId();
        	  }
    	  }
      }
      return "redirect:/" + "home/home";
      
   }
   
   //로그아웃 실행
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
   
   //비밀번호 찾기 페이지
   @GetMapping("/findPw")
   public String getFindPw(Model model, LoginForm loginForm) {
	   model.addAttribute("loginForm", loginForm);
	   return "login/findPw";
   }
   
   //입력한 Email 존재하는지 여부
   @ResponseBody
   @PostMapping("/findPw")
   public String postFindPw(@RequestParam(value = "loginId") String loginId){
	   System.out.println(loginId);
	   int cnt = memberRepository.idCheck(loginId);
	   System.out.println(cnt);
	   if(cnt == 0) {
		   return "0";
	   }else {
		   return "1";
	   }
   }
   
   //Email 전송 및 비밀번호 난수값 저장
   @PostMapping("/findPw/sendEmail")
   @ResponseBody
   public String postFindPwSendMail(
		   @RequestParam(value = "loginId") String loginId) throws Exception{
	   String ePw = emailService.sendSimpleMessagePassword(loginId);
	   
	   System.out.println("controller ePw : "+ePw);
	   Member member = new Member();
	   member.setPassword(passwordEncoder.encode(ePw));
	   member.setLoginId(loginId);
	   memberRepository.updatePasswordByLoginId(member);
	   return "redirect:/login/login";
   }

   //로그인 유효성 검사
   public void validateLoginForm(LoginForm loginForm, Errors errors) {
      if(!StringUtils.hasText(loginForm.getLoginId())) {
         errors.rejectValue("loginId", null, "아이디 필수 입력입니다.");
      }
      if(!StringUtils.hasText(loginForm.getPassWord())) {
         errors.rejectValue("passWord", null, "비밀번호 필수 입력입니다.");
      }
   }
   
   //이메일 형식
   public void validateEmail(String email, Errors errors) {
	    if (!StringUtils.hasText(email)) {
       errors.rejectValue("loginId", null, "아이디 필수 입력입니다.");
	    } else if (email.matches("^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$")) {
       errors.rejectValue("loginId", null, "올바른 이메일 형식이 아닙니다.");
	    }
   }
   
   //회원가입 유효성검사
   public void validateJoinForm(JoinForm joinForm, Errors errors) {
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

   //회원가입시, email 중복 여부 체크
   @ResponseBody
   @PostMapping("/joongbok")
   public String idCheck(@RequestParam("email") String email) {
	   if (!StringUtils.hasText(email)) {
	       return "3";
	    } 
	    if (!email.matches("^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$")) {
	    	return "3";
	    }else {
	        System.out.println("중복체크 진입");
	        System.out.println("email:" + email);
	        int cnt = memberRepository.idCheck(email);
	        System.out.println("cnt : " + cnt);
	        String cntStr = Integer.toString(cnt);
	        return cntStr;
	    }
   }
   
   
   public void aboutNotice(Member member, Model model) {
	      if(member != null) {
	    	  List<Notice> noticeList = aboutPostRepository.selectNoticeById(member.getId());
	          int noticeCnt = 0;
	          int chatCnt = 0;
	          for(int i = 0 ; i < noticeList.size(); i++) {
	        	  if(!noticeList.get(i).getType().equals("chat")&& noticeList.get(i).getView()==0) noticeCnt++;
	        	  else if(noticeList.get(i).getType().equals("chat") && noticeList.get(i).getView()==0) chatCnt++;
	          }
	          model.addAttribute("chatCnt", chatCnt);
	          model.addAttribute("noticeCnt", noticeCnt);  
	      }
	   
   	}
   }