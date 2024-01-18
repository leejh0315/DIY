package project.DIY.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.Likes;
import project.DIY.domain.Member;
import project.DIY.domain.Post;
import project.DIY.domain.Reply;
import project.DIY.domain.ReportPost;
import project.DIY.repository.AboutPostRepository;
import project.DIY.repository.PostRepository;
import project.DIY.repository.ReplyRepository;
import project.DIY.session.SessionVar;
@Controller
@RequiredArgsConstructor
public class AboutPostController {
	@Autowired
	private final AboutPostRepository aboutPostRepository;
	@Autowired
	private final PostRepository postRepository;
	@Autowired
	private final ReplyRepository replyRepository;
	
	@PostMapping("/insertLike")
	@ResponseBody
	public String insertLike(@RequestParam(value = "postCode") int postCode , HttpServletRequest req) {
		Likes likes = new Likes();
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		int userid = member.getId();
		likes.setMemberId(userid);
		likes.setPostCode(postCode);
		
		int cnt = aboutPostRepository.selectLikes(likes);
		System.out.println("이미 들어가 있는 갯수 cnt : " + cnt);
		
		if (cnt ==0) {
			aboutPostRepository.insertLikes(likes);
			return "1";
		}else {
			//likes 가 삭제되어야 한다.
			aboutPostRepository.deleteLikes(likes);
			return "0";
		}
	}
	
	@PostMapping("/reReplyPost/{postCode}")	//대댓
	public String insertReReplyPost( @PathVariable("postCode") String postCode,
			@ModelAttribute Reply reply, HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		reply.setReplyerId(member.getId());
		System.out.println(reply);
		replyRepository.insertReReply(reply);
		
		return "redirect:/posts/{postCode}";
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
