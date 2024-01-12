package project.DIY.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
import project.DIY.domain.Post;
import project.DIY.domain.ReportPost;
import project.DIY.repository.AboutPostRepository;
import project.DIY.repository.MemberRepository;
import project.DIY.session.SessionVar;
@Controller
@RequiredArgsConstructor
public class AdminController {
	@Autowired
	private final AboutPostRepository aboutPostRepository;
	@Autowired
	private final MemberRepository memberRepository;
	

	@GetMapping("/admin")
	public String getAdminPage(Model model, HttpServletRequest req) {
		HttpSession session = req.getSession();
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);

		model.addAttribute("member", member);
		
		List<ReportPost> allReportPost = aboutPostRepository.selectAllReportPost();

		model.addAttribute("allReportPost",allReportPost);
		
		
		
		return "admin/admin";
	}	
	

	@GetMapping("/admin2")
	public String getAdmin2Page(Model model, HttpServletRequest req) {
		HttpSession session = req.getSession();
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);

		model.addAttribute("member", member);
		
		List<Member> allUsers = memberRepository.selectAllUser();
		
		
		model.addAttribute("allUsers",allUsers);
		
		
		return "admin/admin2";
	}	
	
	
	@PostMapping("/updateUserStatusCode")
	@ResponseBody
	public Integer updateUserStatusCode(@RequestParam(value = "statusCode") String statusCode,@RequestParam(value = "id") String id) {
		System.out.println(statusCode);
		System.out.println(id);
		HashMap<String,String> map = new HashMap<String,String>();
		
		if("Y".equals(statusCode)) {
			map.put("statusCode", "N");
	        map.put("id", id);
			System.out.println(map);
			memberRepository.updateUserStatusCode(map);
		}else if("N".equals(statusCode)) {
			 map.put("statusCode", "Y");
		     map.put("id", id);
			System.out.println(map);
			memberRepository.updateUserStatusCode(map);
		}else {
			System.out.println("잘못됏다 여기서");
		}
		
		return 1;
		
		
	}
	

}
