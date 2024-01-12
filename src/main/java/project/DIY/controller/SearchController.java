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
public class SearchController {

	@Autowired
	private final PostRepository postRepository;
	@Autowired
	private final MemberRepository memberRepository;
	
	@PostMapping("/home/search/{keyword}")
	@ResponseBody
	public int postSearchKeyword(@PathVariable("keyword") String keyword) {
		int cnt = postRepository.selecetPosCntBySearch(keyword);
		return cnt;
	}
	
	@GetMapping("/home/searchResult")
	public String getSearchResult(@RequestParam(value = "searchKeyword", required = false) String searchKeyword,
            					  @RequestParam(value = "totalCount", required = false) Integer totalCount,
            					  @RequestParam(value = "page", required = false) Integer page,
            Model model) {
		System.out.println("검색어 : " + searchKeyword);
		System.out.println("총갯수 : " + totalCount);
		System.out.println("페이지 : " + page);
		
		PaginationVo pageVo = new PaginationVo(totalCount, page);
		pageVo.setRowCount(10);
		pageVo.setType(searchKeyword);
		pageVo.setPage(page);
		pageVo.setRowCount(10);
		//page ==1 이면 0
		//page ==2 이면 10
		//page ==3 이면 20
		//page ==4 이면 30
		pageVo.setOffset(1);
		pageVo.setTotalCount(totalCount);
		List<Post> post = postRepository.selecetPostBySearch(pageVo);
		System.out.println(post);
		
		
		
		return "main/searchResult";
	}	
	
	
	
	
}
