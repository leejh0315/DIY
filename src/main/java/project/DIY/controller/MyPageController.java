package project.DIY.controller;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
import project.DIY.domain.PaginationVo;
import project.DIY.domain.Post;
import project.DIY.form.PasswordUpdateForm;
import project.DIY.repository.MemberRepository;
import project.DIY.repository.PostRepository;
import project.DIY.service.PasswordUpdateService;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor
public class MyPageController {

	@Autowired
	private final PostRepository postRepository;
	@Autowired
	private final MemberRepository memberRepository;
	@Autowired
	private final PasswordUpdateService passwordUpdateService;
	@Autowired
	private final PasswordEncoder passwordEncoder;
	
	
	@GetMapping("/myPage/{id}")
	public String getMyPage(@PathVariable("id") String id,HttpServletRequest req, Model model,
			 
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "type", defaultValue = "all") String type
			 
			)throws Exception {
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		List<Post> myPost = postRepository.selectUserPostbyId(Integer.parseInt(id));
		int size = myPost.size();

		
//		for(int i=0; i<myPost.size();i++) {
//			String contentTemp = myPost.get(i).getContent();
//			String plainText = contentTemp.replaceAll("\\<.*?\\>", "");;
//			myPost.get(i).setContent(plainText);
////			System.out.println(plainText);
//		};
		//-------------------------------------------------------------------------------------------
		
		PaginationVo paginationVo = new PaginationVo(myPost.size(), page);
		paginationVo.setMemberId(member.getId());
		paginationVo.setType(type);
		int cnt = postRepository.getPostsCountByMemberId(paginationVo);
		System.out.println("cnt : "+cnt);
		for(int i =0 ;i< cnt; i++) {
			System.out.println(myPost.get(i).getCreateOn());
		}
		
		paginationVo.setOffset((page-1)*5);
		
		List<Post> list = postRepository.getPostsByPageByMemberId(paginationVo);
		
		for(int i=0; i<list.size();i++) {
			String contentTemp = list.get(i).getContent();
			String plainText = contentTemp.replaceAll("\\<.*?\\>", "");;
			plainText = plainText.replaceAll("&nbsp;", "");
			list.get(i).setContent(plainText);
//			System.out.println(plainText);
		}
		
		System.out.println("l.size : " + list.size());
		System.out.println("Math.ceil : " + Math.ceil(cnt/5.0));
		int endPage = (cnt/5 <= 0)? 1 :(int)(Math.ceil(cnt/5.0));  
		System.out.println("endPage :"+ endPage);
		paginationVo.setEndPage(endPage);
		
		System.out.println("pageVo : " +paginationVo);
//	    model.addAttribute("boardList", list);
		model.addAttribute("size",size);
		model.addAttribute("type", type);
	    model.addAttribute("page", page);
	    model.addAttribute("pageVo", paginationVo);
		
		//-------------------------------------------------------------------------------------------
		
	    model.addAttribute("posts", myPost);
		model.addAttribute("post",list);
		model.addAttribute("member", member);
		
		return "myPage/myPage";
	}
	
	@GetMapping("/myPage/update/{id}")
	public String getMyPageUpdate(@PathVariable("id") String id, HttpServletRequest req, Model model) {
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		
		model.addAttribute("member", member);
		
		
		
		return "myPage/updateMember";
	}
	
	@PostMapping("/myPage/update/")
	public String postMyPageUpdate(HttpServletRequest req, @ModelAttribute Member member) {
		HttpSession session = req.getSession(false);
		Member sessionMember = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		
		member.setId(sessionMember.getId());
		String path = Integer.toString(member.getId());
		memberRepository.updateById(member);
		member.setActiveUUID(UUID.randomUUID().toString());
		memberRepository.updateUUID(member);
		session.setAttribute(SessionVar.LOGIN_MEMBER, member);
		
		return "redirect:/myPage/" + path;
	}
	
	
	@RequestMapping(value="/myPage/profileImage", produces="application/json; charset=utf8")
	@ResponseBody
	public String postProfileImage(@RequestParam("file") MultipartFile multipartFile,
            HttpServletRequest request) {
		System.out.println("profileImage Post 요청 접근 완료");
    	// JSON 객체 생성
        JsonObject jsonObject = new JsonObject();

        // 이미지 파일이 저장될 경로 설정
        String fileRoot  = "C:\\DIY\\src\\main\\resources\\static\\image\\profile\\"; 

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
        System.out.println(a);
		
		return a;
	}
	
	@GetMapping("/myPage/passwordUpdate/{id}")
	public String getPasswordUpdate(@PathVariable("id") String id, HttpServletRequest req, Model model, PasswordUpdateForm passwordUpdateForm) {
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		System.out.println(passwordEncoder.encode("12341234"));
		model.addAttribute("passwordUpdateForm", passwordUpdateForm);
		model.addAttribute("member", member);
		return "myPage/updatePassword";
	}
	
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
		
		String originPasswordCheck = passwordUpdateForm.getOriginPassword();

		String type;
		if(passwordUpdateForm.getType().equals("sixMonth")) {
			type = "/myPage/updatePassword";
		}else {
			type = "/mypage/directlyUpdatepassword";
		}
		
		passwordUpdateService.validatePasswordUpdateForm(passwordUpdateForm, bindingResult, originPasswordCheck, member.getLoginId());
		
		 if(bindingResult.hasErrors()) {
			 System.out.println("에러");
	    	  model.addAttribute("passwordUpdateForm", passwordUpdateForm);
	    	  model.addAttribute("member", member);
	         return type;
		 }else {
			 member.setPassword(passwordEncoder.encode(passwordUpdateForm.getNewPassword()));
			 memberRepository.updatePasswordById(member);
			 SecurityContextHolder.clearContext();
			 session.invalidate();
	         return "redirect:/" + "home/dologin";   
	      }
	}
	
	@GetMapping("/directlyUpdatepassword/{id}")
	public String getDirectlyUpdatepassword(@ModelAttribute PasswordUpdateForm passwordUpdateForm, Model model,
			@PathVariable("id") String id,
			BindingResult bindingResult,  HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		model.addAttribute("member", member);
		model.addAttribute("passwordUpdateForm", passwordUpdateForm);		
		return "myPage/directlyUpdatepassword";
	}
	
//	@PostMapping("directlyUpdatepassword/{id}")
//	public String postDirectlyUpdatepassword(@ModelAttribute PasswordUpdateForm passwordUpdateForm) {
//		System.out.println("요청 옴");
//		return "redirect:/home/dologin";
//	}
}

