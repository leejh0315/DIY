package project.DIY.controller;

import java.io.File;
import java.util.ArrayList;
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
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		if(!member.getLoginId().equals("admin")) {
			return "redirect:/home/home";
		}
		
		List<Post> allPost = postReposiroty.selectAllpost();
		getImages(allPost);
		
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
		if(!member.getLoginId().equals("admin")) {
			return "redirect:/home/home";
		}

		model.addAttribute("member", member);
		model.addAttribute("allPosts", allPosts);
		
		return "admin/postManage";
	}

	@GetMapping("/admin/userManage")
	public String getuserManagePage(Model model, HttpServletRequest req) {
		HttpSession session = req.getSession();
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		if(!member.getLoginId().equals("admin")) {
			return "redirect:/home/home";
		}

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
	
	

	
	
	public  void getImages(List<Post> allPost) {
		
		
		String directoryPath = "C:\\DIY\\src\\main\\resources\\static\\image\\post\\";
		
		
        // File 객체를 생성하여 해당 경로의 디렉터리를 나타내도록 합니다.
        File directory = new File(directoryPath);
        // 디렉터리가 존재하는지 확인합니다.
        if (directory.exists() && directory.isDirectory()) {
            // 디렉터리 내의 파일 리스트를 가져옵니다.
            File[] files = directory.listFiles(); 

            // 파일 리스트를 순회하며 각 파일의 정보를 출력합니다.
            for (File file : files) {
                int num = 0;
                // 파일명 출력
                /*
                System.out.println("File name: " + file.getName());
                // 파일 경로 출력
                System.out.println("File path: " + file.getAbsolutePath());
                // 파일 크기 출력
                System.out.println("File size (bytes): " + file.length());
                // 파일이 디렉터리인지 여부 출력
                System.out.println("Is directory: " + file.isDirectory());
                System.out.println("-----------------------------");
                */
                
                for(int i = 0 ; i < allPost.size(); i++) {
                	String a = allPost.get(i).getContent();
                	if(a.contains(file.getName())) {
                		num ++;
                	}
                	
                }
                if(num == 0) {
                	file.delete();
                }
                
            }
        } else {
            System.out.println("Directory does not exist or is not a directory.");
        }
    }

}
