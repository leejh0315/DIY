package project.DIY.controller;

import java.util.HashMap;
import java.util.List;

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
import project.DIY.repository.PostRepository;
import project.DIY.repository.ReplyRepository;
import project.DIY.session.SessionVar;
@Controller
@RequiredArgsConstructor
public class AdminController {
	@Autowired
	private final AboutPostRepository aboutPostRepository;
	@Autowired
	private final MemberRepository memberRepository;
	@Autowired
	private final PostRepository postReposiroty;
	@Autowired
	private final ReplyRepository replyReposiroty;
	
	@GetMapping("/admin/reportPostManage")
	public String getreportPostManagePage(Model model, HttpServletRequest req) {
		HttpSession session = req.getSession();
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);

		model.addAttribute("member", member);
		
		List<ReportPost> allReportPost = aboutPostRepository.selectAllReportPost();
		for(int i=0; i<allReportPost.size();i++) {
			String contentTemp = allReportPost.get(i).getContent();
			String plainText = contentTemp.replaceAll("\\<.*?\\>", "");;
			plainText = plainText.replaceAll("&nbsp;", "");
			plainText = plainText.replaceAll("&gt;", "");
			//글자수 25자만 가져오기
			int maxLength = 25;
	        if (plainText.length() > maxLength) {
	            plainText = plainText.substring(0, maxLength)+ "...";
	        }
			allReportPost.get(i).setContent(plainText);
		}
		model.addAttribute("allReportPost",allReportPost);
		
		
		
		return "admin/reportPostManage";
	}	
	
	@GetMapping("/admin/PostManage")
	public String getPostManagePage(Model model, HttpServletRequest req) {
		HttpSession session = req.getSession();
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
//		member.getLoginId().equals("admin");
		List<Post> allPosts = postReposiroty.selectAllpost();
		
		

		model.addAttribute("member", member);
		model.addAttribute("allPosts", allPosts);
		System.out.println(allPosts);
		
		
		
		return "admin/postManage";
	}

	@GetMapping("/admin/userManage")
	public String getuserManagePage(Model model, HttpServletRequest req) {
		HttpSession session = req.getSession();
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);

		model.addAttribute("member", member);
		
		List<Member> allUsers = memberRepository.selectAllUser();
		
		
		model.addAttribute("allUsers",allUsers);
		
		
		return "admin/userManage";
	}	
	
	
	
	@PostMapping("/deletePost")
	@ResponseBody
	public String deletePost(@RequestParam(value = "postCode") int postCode) {
		postReposiroty.deletePost(postCode);
		aboutPostRepository.deletePostLikes(postCode);
		aboutPostRepository.deleteReportPost(postCode);
		replyReposiroty.deleteReplybypostCode(postCode);
		return "1";
		
	}
	
	@PostMapping("/updateUserStatusCode")
	@ResponseBody
	public String updateUserStatusCode(@RequestParam(value = "statusCode") String statusCode,@RequestParam(value = "id") String id) {
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
			
		}
		
		return "1";
		
		
	}
	

}
