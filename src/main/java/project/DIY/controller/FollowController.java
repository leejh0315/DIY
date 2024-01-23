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
import project.DIY.repository.FollowRepository;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor
public class FollowController {
	@Autowired
	private final FollowRepository followRepository;
	
	@PostMapping("/insertFollow") //팔로잉 기능
	@ResponseBody
	public String insertFollow(@RequestParam(value = "followee") int followee ,  HttpServletRequest req) {
		
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		int userid = member.getId();
		
		int cnt = followRepository.followCheck(userid, followee);
		
		if (cnt == 0) {
			//팔로잉중 아니면 팔로우
			followRepository.insertFollow(userid, followee);
			return "1";
		} else {
			//이미 팔로잉중이면 언팔로우
			followRepository.unfollow(userid, followee);
			return "0";
		}
		
		
		
	}
}
