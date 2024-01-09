package project.DIY.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
import project.DIY.repository.AboutPostRepository;
import project.DIY.session.SessionVar;
@Controller
@RequiredArgsConstructor
public class AboutPostController {
	@Autowired
	private final AboutPostRepository aboutPostRepository;
	
	@PostMapping("/insertLike")
	@ResponseBody
	public Integer insertLike(@RequestParam(value = "postCode") int postCode , HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		int userid = member.getId();
		
		aboutPostRepository.insertLikes(userid, postCode);
		return 1;
	}
	
}
