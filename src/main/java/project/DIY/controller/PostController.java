package project.DIY.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
import project.DIY.domain.Post;
import project.DIY.repository.PostRepository;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor
public class PostController {
	private final PostRepository postRepository;
	
	@GetMapping("/writePost")
	public String getPost(Model model) {
		model.addAttribute("writePost", new Post());
		return "post/writePost";
	}
	
    @PostMapping("/writePost")                                                        
    public String setArticle(@ModelAttribute Post post, Model model) {                
    	System.out.println(post);
    	//postRepository.insertPost(post);
    	//return "redirect:/article/" + post.getPostCode();
    	return "redirect:/";
    }
	
//	@PostMapping("/writePost")
//	public String postPost(@ModelAttribute Post post, HttpServletRequest req) {
//		
//		
//		System.out.println(post);
//		
//		//HttpSession session = req.getSession(false);
//		//Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
//		//System.out.println(member);
//		//postRepository.insertPost(post);
//		return "redirect:/";
//	} 
	
	@GetMapping("/selectPost")
	public String selectPost(Model model) {
		Post post = postRepository.selectPost();
		model.addAttribute("post", post);
		return "post/selectPost";
	}
}
