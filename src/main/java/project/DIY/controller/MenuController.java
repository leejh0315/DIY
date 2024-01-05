package project.DIY.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.Post;
import project.DIY.repository.PostRepository;

@Controller
@RequiredArgsConstructor
public class MenuController {
	
	private final PostRepository postRepository;
	
	@GetMapping("/menu/{type}")
	public String getBook(Model model, @PathVariable("type") String type, HttpServletRequest req) {
		
		List<Post> post = postRepository.selectByType(type);
		System.out.println(post);
		
		if(type.equals("book")) {
			model.addAttribute("typeName", "책");
		}else if(type.equals("movie")) {
			model.addAttribute("typeName", "영화");
		}else {
			model.addAttribute("typeName", "공연");
		}
		
		model.addAttribute("type", post);
		
		return "menu/type";
	}

}
