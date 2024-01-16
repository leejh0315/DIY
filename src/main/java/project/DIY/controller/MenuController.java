package project.DIY.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
import project.DIY.domain.Post;
import project.DIY.repository.MemberRepository;
import project.DIY.repository.PostRepository;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor
public class MenuController {
	
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	
	@GetMapping("/menu/{type}")
	public String getBook(Model model, @PathVariable("type") String type, HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);

		List<Post> post = postRepository.selectByType(type);
		
		if(type.equals("book")) {
			model.addAttribute("typeName", "책");
		}else if(type.equals("movie")) {
			model.addAttribute("typeName", "영화");
		}else {
			model.addAttribute("typeName", "공연");
		}
		
		 List<String> memberprofileimg =new ArrayList<>();
		 
		 for(int i = 0; i<post.size();i++) {
	    	  Integer memberId =((Post) post.get(i)).getMemberId();
	    	  
	    	  
		   	  if(memberRepository.selectBymemberId(memberId).getProfileSrc()==null) {
		   		  memberprofileimg.add("/img/defalut_profileimg.jpg");
		    	  }
		  	  else { memberprofileimg.add(memberRepository.selectBymemberId(memberId).getProfileSrc());}
		    	 
	      }
		//System.out.println(memberprofileimg.get(0));
		model.addAttribute("member", member);
		model.addAttribute("type", post);
		model.addAttribute("profilesrc",memberprofileimg);
		return "menu/type";
	}

}
