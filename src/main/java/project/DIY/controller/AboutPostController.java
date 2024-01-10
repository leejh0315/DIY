package project.DIY.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.Likes;
import project.DIY.domain.Member;
import project.DIY.domain.Post;
import project.DIY.domain.ReportPost;
import project.DIY.repository.AboutPostRepository;
import project.DIY.repository.PostRepository;
import project.DIY.session.SessionVar;
@Controller
@RequiredArgsConstructor
public class AboutPostController {
	@Autowired
	private final AboutPostRepository aboutPostRepository;
	@Autowired
	private final PostRepository postRepository;
	
	@PostMapping("/insertLike")
	@ResponseBody
	public Integer insertLike(@RequestParam(value = "postCode") int postCode , HttpServletRequest req) {
		Likes likes = new Likes();
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		int userid = member.getId();
		likes.setMemberId(userid);
		likes.setPostCode(postCode);
		
		int cnt = aboutPostRepository.selectLikes(likes);
		
		if (cnt ==0) {
			aboutPostRepository.insertLikes(likes);
			return 1;
		}else {
			//likes 가 삭제되어야 한다.
			aboutPostRepository.deleteLikes(likes);
			return 0;
		}
		
	
	
	
	}
	
	@PostMapping("/insertReportPost")
	@ResponseBody
	public Integer insertReportPost(@RequestParam(value = "postCode") int postCode, HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		Post currentPost = postRepository.selectByPostCode(postCode);
		int userid = member.getId();
		
	
		
		ReportPost reportPost = new ReportPost();
		
		reportPost.setPostCode(postCode);
		reportPost.setTitle(currentPost.getTitle());
		reportPost.setContent(currentPost.getContent());
		reportPost.setMemberId(currentPost.getMemberId());
		reportPost.setReporterId(userid);
		
		System.out.println(reportPost);
		
		int selectReportPostcnt = aboutPostRepository.selectReportPost(reportPost);
		
		System.out.println(selectReportPostcnt);
		if (selectReportPostcnt == 0) {
			aboutPostRepository.insertReportPost(reportPost);
			return 1;
		}else {
			return 0;
		}
		
		
		
	}
	
	
}
