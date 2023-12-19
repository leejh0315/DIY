package project.DIY.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.Post;
import project.DIY.repository.PostRepository;

@Controller
@RequiredArgsConstructor
public class PostController {
	private final PostRepository postRepository;
	
	@GetMapping("/post")
	public String getPost(Model model) {
		model.addAttribute("post", new Post());
		return "post/post";
	}
	
	@PostMapping("/post")
	public String postPost(@ModelAttribute Post post) {
		System.out.println(post);
		postRepository.insertPost(post);
		return "redirect:/";
	} 
	
}
