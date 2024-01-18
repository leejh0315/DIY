package project.DIY.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.PaginationVo;
import project.DIY.domain.Post;
import project.DIY.repository.MemberRepository;
import project.DIY.repository.PostRepository;

@Controller
@RequiredArgsConstructor
public class SearchController {		//검색 관련 controller

	@Autowired
	private final PostRepository postRepository;
	@Autowired
	private final MemberRepository memberRepository;
	
	
	//검색시, 검색어에 맞는 결과의 갯수를 반환
	@PostMapping("/home/search/{keyword}")
	@ResponseBody
	public String postSearchKeyword(@PathVariable("keyword") String keyword) {
		int cnt = postRepository.selecetPosCntBySearch(keyword);
		return Integer.toString(cnt);
	}
	
	//검색어에 따른 결과를 반환, html에 전달
	@GetMapping("/home/searchResult")
	public String getSearchResult(@RequestParam(value = "searchKeyword", required = false) String searchKeyword,
            					  @RequestParam(value = "totalCount", required = false) Integer totalCount,
            					  @RequestParam(value = "page", required = false) Integer page,
            					  Model model) {
		
		PaginationVo pageVo = new PaginationVo(totalCount, page);
		pageVo.setRowCount(10);
		pageVo.setType(searchKeyword);
		pageVo.setPage(page);
		pageVo.setRowCount(10);
		pageVo.setOffset((page-1)*10);
		pageVo.setTotalCount(totalCount);
		List<Post> post = postRepository.selecetPostBySearch(pageVo);
		model.addAttribute("post", post);
		model.addAttribute("searchKeyword", searchKeyword);
		return "main/searchResult";
	}	
	
	
	
	
}
