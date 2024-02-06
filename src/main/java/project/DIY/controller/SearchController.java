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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
import project.DIY.domain.Notice;
import project.DIY.domain.PaginationVo;
import project.DIY.domain.Post;
import project.DIY.repository.AboutPostRepository;
import project.DIY.repository.PostRepository;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor
public class SearchController {		//검색 관련 controller

	@Autowired
	private final PostRepository postRepository;
	@Autowired
	private final AboutPostRepository aboutPostRepository;
	
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
            					  HttpServletRequest req,
            					  Model model) {
		
		HttpSession session = req.getSession();
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		aboutNotice(member, model);
		PaginationVo pageVo = new PaginationVo(totalCount, page);
		pageVo.setRowCount(10);
		pageVo.setType(searchKeyword);
		pageVo.setPage(page);
		pageVo.setRowCount(10);
		pageVo.setOffset((page-1)*10);
		pageVo.setTotalCount(totalCount);
		List<Post> post = postRepository.selecetPostBySearch(pageVo);
		model.addAttribute("member", member);
		model.addAttribute("post", post);
		model.addAttribute("searchKeyword", searchKeyword);
		return "main/searchResult";
	}	
	
	
	   public void aboutNotice(Member member, Model model) {
		      if(member != null) {
		    	  List<Notice> noticeList = aboutPostRepository.selectNoticeById(member.getId());
		          int noticeCnt = 0;
		          int chatCnt = 0;
		          for(int i = 0 ; i < noticeList.size(); i++) {
		        	  if(!noticeList.get(i).getType().equals("chat")&& noticeList.get(i).getView()==0) noticeCnt++;
		        	  else if(noticeList.get(i).getType().equals("chat") && noticeList.get(i).getView()==0) chatCnt++;
		          }
		          model.addAttribute("chatCnt", chatCnt);
		          model.addAttribute("noticeCnt", noticeCnt);  
		      }
		   
	   	}
	
}
