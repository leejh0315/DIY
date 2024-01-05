package project.DIY.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
import project.DIY.domain.Reply;
import project.DIY.repository.ReplyRepository;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor
public class ReplyController {
	private final ReplyRepository replyRepository;
	
	@PostMapping("/replypost")
	public String postReply(@ModelAttribute Reply reply, Model model, HttpServletRequest req, RedirectAttributes rAttr) {
		
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		
		
		reply.setReplyerId(member.getId());
		System.out.println(reply);
		replyRepository.insertReply(reply);
		rAttr.addAttribute("root", reply.getPostId());

		return "redirect:/posts/{root}";
	}
	
	
}
