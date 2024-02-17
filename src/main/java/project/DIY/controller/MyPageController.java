package project.DIY.controller;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
import project.DIY.domain.Notice;
import project.DIY.domain.PaginationVo;
import project.DIY.domain.PasswordHistory;
import project.DIY.domain.Post;
import project.DIY.form.JoinForm;
import project.DIY.form.PasswordUpdateForm;
import project.DIY.repository.AboutPostRepository;
import project.DIY.repository.FollowRepository;
import project.DIY.repository.MemberRepository;
import project.DIY.repository.PostRepository;
import project.DIY.service.PasswordUpdateService;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor
public class MyPageController {	//myPage 관련

	@Autowired
	private final PostRepository postRepository;
	@Autowired
	private final AboutPostRepository aboutPostRepository;
	@Autowired
	private final MemberRepository memberRepository;
	@Autowired
	private final PasswordUpdateService passwordUpdateService;
	@Autowired
	private final PasswordEncoder passwordEncoder;
	@Autowired
	private final FollowRepository followRepository;
	
	//마이페이지 접근
	@GetMapping("/myPage/{id}")
	public String getMyPage(@PathVariable("id") int id,HttpServletRequest req, Model model,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "type", defaultValue = "all") String type,
			@RequestParam(value="pgroup", defaultValue="myposts") String pgroup)
			throws Exception {
		
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		
		Member memberVo = memberRepository.selectBymemberId(id);
		aboutNotice(member, model);
		if(id != member.getId()) {
			return "redirect:/home/home";
		}
		

//		팔로잉/팔로워 수	
		int following = followRepository.cntFollowee(id);
		int follower = followRepository.cntFollower(id);
		model.addAttribute("following",following);
		model.addAttribute("follower",follower);
		

		List<Post> myPost = postRepository.selectUserPostbyId(id);
		int size = myPost.size();

		/*
		List<String> memberprofileimg =new ArrayList<>();
		 
		 for(int i = 0; i<myPost.size();i++) {
	    	  Integer memberId =((Post) myPost.get(i)).getMemberId();
		   	  memberprofileimg.add(memberRepository.selectBymemberId(memberId).getProfileSrc());
		    	 
	      }
		 System.out.println(memberprofileimg);
		 model.addAttribute("profilesrc",memberprofileimg);
		 */
		 
		 
		LocalDate currentDate = LocalDate.now();
		int thisMonth = currentDate.getMonthValue();
		int thisyear = currentDate.getYear();
		
		
		LocalDate lastMonthDate = currentDate.minusMonths(1);
		int lastMonth = lastMonthDate.getMonthValue();
		int lastMonthyear = lastMonthDate.getYear();
		
		LocalDate last2MonthDate = currentDate.minusMonths(2);
		int last2Month = last2MonthDate.getMonthValue();
		int last2Monthyear = last2MonthDate.getYear();
		
		int[] dateArr = {last2Monthyear, lastMonthyear, thisyear, 
							last2Month, lastMonth, thisMonth};
		int[] cntArr = new int[6];
		for(int i=0;i<3;i++) {
			cntArr[i+3] = postRepository.countByMonth(id, dateArr[i], dateArr[i+3]);
			cntArr[i] = dateArr[i+3];
		}
		
		model.addAttribute("cntArr", cntArr);
		
		PaginationVo paginationVo = new PaginationVo(myPost.size(), page);
		paginationVo.setMemberId(memberVo.getId());
		paginationVo.setType(type);
		
		
		
//		여기수정
		paginationVo.setPgourp(pgroup);
		
		
		
		
		
//		int cnt = postRepository.getPostsCountByMemberId(paginationVo);
		int cnt;

		
		
		List<Post> pgroupPosts = new ArrayList<Post>();
		List<Post> list = new ArrayList<Post>();
		List<String> profileSrc =new ArrayList<String>();
		if(pgroup.equals("myfeed")) {
			
			List<Post> myFeedPost = postRepository.myFeedPost(id, type);
			cnt = myFeedPost.size();
			list = postRepository.myFeedPosts(paginationVo);
			for(int i =0;i<list.size();i++) {
				profileSrc.add(memberRepository.selectBymemberId(list.get(i).getMemberId()).getProfileSrc());
			}
//			List<Follow> followee = followRepository.selectFollowee(id);
//			
//			
//			for(int i = 0;i<followee.size(); i++) {
//				List<Post> temp= postRepository.selectUserPostbyId(followee.get(i).getFollowee());
//				for(int j = 0;j<temp.size(); j++) {
//					pgroupPosts.add(temp.get(i));
//				}
//			}
		
		}else if(pgroup.equals("likedposts")) {
			
			List<Post> likedPost = postRepository.likedPost(id, type);
			cnt = likedPost.size();
			list = postRepository.likedPosts(paginationVo);
			for(int i =0;i<list.size();i++) {
				profileSrc.add(memberRepository.selectBymemberId(list.get(i).getMemberId()).getProfileSrc());
			}
//			List<Integer> postCodeList = aboutpostRepository.selectLikedPostsById(id);
//			
//			for(int i = 0;i<postCodeList.size(); i++) {
//				Post temp= postRepository.selectByPostCode(postCodeList.get(i));
//				pgroupPosts.add(temp);
//			}
			
		}else {
			cnt = postRepository.getPostsCountByMemberId(paginationVo);
			list = postRepository.getPostsByPageByMemberId(paginationVo);
			for(int i =0;i<list.size();i++) {
				profileSrc.add(memberRepository.selectBymemberId(list.get(i).getMemberId()).getProfileSrc());
			}
			
		}
		System.out.println("--------------------");
		System.out.println("cnt :" + cnt);
		System.out.println("--------------------");
		paginationVo.setOffset((page-1)*5);
		
		
		for(int i=0; i<list.size();i++) {
			String contentTemp = list.get(i).getContent();
			String plainText = contentTemp.replaceAll("\\<.*?\\>", "");;
			plainText = plainText.replaceAll("&nbsp;", "");
			plainText = plainText.replaceAll("&gt;", "");
			list.get(i).setContent(plainText);
		}
		
		int endPage = (cnt/5 <= 0)? 1 :(int)(Math.ceil(cnt/5.0));  
		paginationVo.setEndPage(endPage);
		
		model.addAttribute("pgroupPosts",pgroupPosts);
		
		model.addAttribute("pgroup",pgroup);	 
		
		model.addAttribute("profileSrc",profileSrc);
		
		
		model.addAttribute("size",size);
		model.addAttribute("type", type);
	    model.addAttribute("page", page);
	    model.addAttribute("pageVo", paginationVo);
	    model.addAttribute("posts", myPost);
		model.addAttribute("post",list);
		model.addAttribute("memberVo", memberVo);
		model.addAttribute("member",member);
		return "myPage/myPage";
	}
	
	//회원 정보 수정 페이지
	@GetMapping("/myPage/update/{id}")
	public String getMyPageUpdate(@PathVariable("id") String id, HttpServletRequest req, Model model, JoinForm joinForm) {
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		aboutNotice(member, model);
		if(!id.equals(Integer.toString(member.getId()))) {
			return "redirect:/home/home";
		}
		model.addAttribute("joinForm", joinForm);
		model.addAttribute("member", member);
		return "myPage/updateMember";
	}
	
	//회원 정보 수정 DB update
	@PostMapping("/myPage/update/")
	public String postMyPageUpdate(HttpServletRequest req, @ModelAttribute Member member, Errors errors, BindingResult bindingResult) {
		HttpSession session = req.getSession(false);
		Member sessionMember = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		
		List<Post> posts = postRepository.selectUserPostbyId(sessionMember.getId());
		String newNickName = member.getNickName();
		/*
	    if (!StringUtils.hasText(joinForm.getNickName())) {
	        errors.rejectValue("nickName", null, "닉네임을 입력해주세요.");
	    } else if (!joinForm.getNickName().matches("^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$")) {
	        errors.rejectValue("nickName", null, "닉네임은 특수문자를 제외한 2~10자리여야 합니다.");
	    }
	    */
		if(member.getNickName() == "" || member.getNickName() == null || member.getNickName().trim() == "" || member.getNickName().trim() == null) {
			errors.rejectValue("nickName", null, "닉네임을 입력해주세요.");
			return "myPage/update";
		}else if(!member.getNickName().matches("^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$")) {
			errors.rejectValue("nickName", null, "닉네임은 특수문자를 제외한 2~10자리여야 합니다.");
			return "myPage/update";
		}
		
		for(int i =0; i<posts.size(); i++) {
			postRepository.updateById(newNickName, Integer.parseInt(posts.get(i).getPostCode()));
		}
		
		member.setId(sessionMember.getId());
		String path = Integer.toString(member.getId());
		LocalDateTime currentDateTime = LocalDateTime.now();
		member.setUpdateOn(currentDateTime);
		memberRepository.updateById(member);
		member.setActiveUUID(UUID.randomUUID().toString());
		memberRepository.updateUUID(member);
		session.setAttribute(SessionVar.LOGIN_MEMBER, member);
		
		return "redirect:/myPage/" + path;
	}
	
	//프로필 이미지 변경시 img/profile에 저장
	@RequestMapping(value="/myPage/profileImage", produces="application/json; charset=utf8")
	@ResponseBody
	public String postProfileImage(@RequestParam("file") MultipartFile multipartFile,
            HttpServletRequest request) {
		System.out.println("profileImage Post 요청 접근 완료");
    	// JSON 객체 생성
        JsonObject jsonObject = new JsonObject();
        // 이미지 파일이 저장될 경로 설정
        String fileRoot  = "/home/ubuntu/DIY/src/main/resources/static/image/profile/"; 
        // 업로드된 파일의 원본 파일명과 확장자 추출
        String originalFileName = multipartFile.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // 새로운 파일명 생성 (고유한 식별자 + 확장자)
        String savedFileName = UUID.randomUUID() + extension;
        // 저장될 파일의 경로와 파일명을 나타내는 File 객체 생성
        File targetFile = new File(fileRoot + savedFileName);
        try {
            // 업로드된 파일의 InputStream 얻기
            java.io.InputStream fileStream = multipartFile.getInputStream();

            // 업로드된 파일을 지정된 경로에 저장
            FileUtils.copyInputStreamToFile(fileStream, targetFile);

            // JSON 객체에 이미지 URL과 응답 코드 추가
            jsonObject.addProperty("src", "/image/profile/" + savedFileName);
            jsonObject.addProperty("responseCode", "success");
        } catch (IOException e) {
            // 파일 저장 중 오류가 발생한 경우 해당 파일 삭제 및 에러 응답 코드 추가
            FileUtils.deleteQuietly(targetFile);
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }
        // JSON 객체를 문자열로 변환하여 반환
        String a = jsonObject.toString();
		
		return a;
	}
	
	//비밀번호 변경 페이지
	@GetMapping("/myPage/passwordUpdate/{id}")
	public String getPasswordUpdate(@PathVariable("id") String id, HttpServletRequest req, Model model, PasswordUpdateForm passwordUpdateForm) {
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		aboutNotice(member, model);
		model.addAttribute("passwordUpdateForm", passwordUpdateForm);
		model.addAttribute("member", member);
		return "myPage/updatePassword";
	}
	
	//
	@GetMapping("/password/reAlert/{id}")
	public String passwordUpdateReAlertUser(@PathVariable("id") String id) {
		memberRepository.reAlertUpdatePassword(Integer.parseInt(id));
		return "redirect:/home/home";
	}
	
	@PostMapping("/myPage/updatePassword/{id}")
	public String postPasswordUpdate(@ModelAttribute PasswordUpdateForm passwordUpdateForm, Model model,
			@PathVariable("id") String id,
			BindingResult bindingResult,  HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		aboutNotice(member, model);
		String originPasswordCheck = passwordUpdateForm.getOriginPassword();
		String type;
		if(passwordUpdateForm.getType().equals("sixMonth")) {
			type = "/myPage/updatePassword";
		}else {
			type = "/mypage/directlyUpdatepassword";
		}
		
		passwordUpdateService.validatePasswordUpdateForm(passwordUpdateForm, bindingResult, originPasswordCheck, member.getLoginId());
		
		 if(bindingResult.hasErrors()) {
	    	  model.addAttribute("passwordUpdateForm", passwordUpdateForm);
	    	  model.addAttribute("member", member);
	         return type;
		 }else {
	         LocalDateTime currentDateTime = LocalDateTime.now();
			 member.setPassword(passwordEncoder.encode(passwordUpdateForm.getNewPassword()));
			 member.setPasswordUpdate(currentDateTime);
			 memberRepository.updatePasswordById(member);
			 PasswordHistory pH = new PasswordHistory();
			 pH.setMemberId(member.getId());
			 pH.setPassword(passwordEncoder.encode(passwordUpdateForm.getNewPassword()));
			 memberRepository.insertPasswordHistory(pH);
			 SecurityContextHolder.clearContext();
			 session.invalidate();
	         return "redirect:/" + "home/dologin";   
	      }
	}
	
	//비밀번호 직접 수정
	@GetMapping("/directlyUpdatepassword/{id}")
	public String getDirectlyUpdatepassword(@ModelAttribute PasswordUpdateForm passwordUpdateForm, Model model,
			@PathVariable("id") String id,
			BindingResult bindingResult,  HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		aboutNotice(member, model);
		model.addAttribute("member", member);
		model.addAttribute("passwordUpdateForm", passwordUpdateForm);		
		return "myPage/directlyUpdatepassword";
	}
	
	@GetMapping("/memberOut/{id}")
	public String getMemberOut(Model model,
			@PathVariable("id") String id,
			HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		
		if(member.getId() != Integer.parseInt(id)) {
			return "redirect:/home/home";
		}
		
		aboutNotice(member, model);
		model.addAttribute("member", member);
		return "myPage/memberOut";
	}
	
	@PostMapping("/memberOut/{id}")
	@ResponseBody
	public String postMemberOut(@PathVariable("id") String id, HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		memberRepository.deleteMemberById(id);
		memberRepository.deletePostByMemberId(id);
		memberRepository.deleteReplyByMemberId(id);
		memberRepository.deleteReReplyByMemberId(id);
		memberRepository.deleteReReplyByMemberId(id);
		memberRepository.deleteChatRoomByMemberId(id);
		memberRepository.deleteFollowByMemberId(id);
		memberRepository.deleteLikePostByMemberId(id);
		memberRepository.deleteNocieByMemberId(id);
		memberRepository.deletePasswordHistoryByMemberId(id);
		session.invalidate();
		return "done";
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

