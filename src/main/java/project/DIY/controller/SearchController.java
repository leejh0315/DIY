package project.DIY.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.PaginationVo;
import project.DIY.repository.MemberRepository;
import project.DIY.repository.PostRepository;

@Controller
@RequiredArgsConstructor
public class SearchController {

	@Autowired
	private final PostRepository postRepository;
	@Autowired
	private final MemberRepository memberRepository;
	
	@PostMapping("/search/{keyword}")
	public String postSearchKeyword(@PathVariable("keyword") String keyword) {
		int cnt = postRepository.selecetPosCnttBySearch(keyword);
		
		PaginationVo page = new PaginationVo(0,1);
		
	
		
		return "main/searchResult";
	}
	
	
}
