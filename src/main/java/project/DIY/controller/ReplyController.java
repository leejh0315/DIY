package project.DIY.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
import project.DIY.domain.Notice;
import project.DIY.domain.Post;
import project.DIY.domain.Reply;
import project.DIY.repository.AboutPostRepository;
import project.DIY.repository.PostRepository;
import project.DIY.repository.ReplyRepository;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor
public class ReplyController {
	@Autowired
	private final ReplyRepository replyRepository;
	@Autowired
	private final PostRepository postRepository;
	@Autowired
	private final AboutPostRepository aboutPostRepository; 
	
	
	//댓글 작성하면, 댓글 테이블에 등록
	@PostMapping("/replypost")
	public String postReply(@ModelAttribute Reply reply, Model model, HttpServletRequest req, RedirectAttributes rAttr) {
		
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		
		reply.setReplyerId(member.getId());
		LocalDateTime currentDateTime = LocalDateTime.now();
		reply.setReplyCreateDate(currentDateTime);
		replyRepository.insertReply(reply);
		rAttr.addAttribute("root", reply.getPostId());
		
		Post post = postRepository.selectByPostCode(reply.getPostId());

		Notice notice = new Notice();
		notice.setDoMemberId(member.getId());
		notice.setTargetMemberId(post.getMemberId());
		notice.setType("reply");
		notice.setTargetId(reply.getPostId());
		
		aboutPostRepository.insertNotice(notice);
		return "redirect:/posts/{root}";
	}
	
	
}
