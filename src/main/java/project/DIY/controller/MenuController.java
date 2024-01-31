package project.DIY.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
import project.DIY.domain.Notice;
import project.DIY.domain.PaginationVo;
import project.DIY.domain.Post;
import project.DIY.repository.AboutPostRepository;
import project.DIY.repository.MemberRepository;
import project.DIY.repository.PostRepository;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor
public class MenuController {
	
	@Autowired
	private final PostRepository postRepository;
	@Autowired
	private final MemberRepository memberRepository;
	@Autowired
	private final AboutPostRepository aboutPostRepository;
	//타입에 맞는 게시물 출력
	@GetMapping("/menu/{type}")
	public String getBook(Model model, @PathVariable("type") String type, HttpServletRequest req,
			@RequestParam(value = "page", defaultValue = "1") int page
			) {
		HttpSession session = req.getSession();
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		aboutNotice(member, model);
		int postcnt = postRepository.selectByTypeCnt(type);
		
		PaginationVo pageVo = new PaginationVo(postcnt, page);
		
		pageVo.setOffset((page-1) * 5);
		pageVo.setType(type);
		
		List<Post> post = postRepository.selectByType(pageVo);
		
		for (int i = 0 ; i < post.size(); i++) {
			LocalDateTime a = post.get(i).getCreateOn();
			System.out.println(a);
		};
		
		if(type.equals("book")) {
			model.addAttribute("typeEng" , "book");
			model.addAttribute("typeName", "책");
		}else if(type.equals("movie")) {
			model.addAttribute("typeEng" , "movie");
			model.addAttribute("typeName", "영화");
		}else {
			model.addAttribute("typeEng" , "concert");
			model.addAttribute("typeName", "공연");
		}
		
		 List<String> memberprofileimg =new ArrayList<>();
		 for(int i = 0; i<post.size();i++) {
	    	  Integer memberId =post.get(i).getMemberId();
			  memberprofileimg.add(memberRepository.selectBymemberId(memberId).getProfileSrc());
	      }
		 
		 for(int i=0; i<post.size();i++) {
				String contentTemp = post.get(i).getContent();
				String plainText = contentTemp.replaceAll("\\<.*?\\>", "");;
				plainText = plainText.replaceAll("&nbsp;", "");
				plainText = plainText.replaceAll("&gt;", "");
				post.get(i).setContent(plainText);
			}
		 
		model.addAttribute("pageVo", pageVo);
		model.addAttribute("member", member);
		model.addAttribute("type", post);
		model.addAttribute("profilesrc",memberprofileimg);
		return "menu/type";
	}
	
	   public void aboutNotice(Member member, Model model) {
		      if(member != null) {
		    	  List<Notice> noticeList = aboutPostRepository.selectNoticeById(member.getId());
		          int noticeCnt = 0;
		          for(int i = 0 ; i < noticeList.size(); i++) {
		        	  if(noticeList.get(i).getView()==0) noticeCnt++;
		          }
		          model.addAttribute("noticeCnt", noticeCnt);  
		      }
		   
	   	}

}
