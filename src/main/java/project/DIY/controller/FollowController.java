package project.DIY.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.Follow;
import project.DIY.domain.Member;
import project.DIY.domain.PaginationVo;
import project.DIY.domain.Post;
import project.DIY.repository.FollowRepository;
import project.DIY.repository.MemberRepository;
import project.DIY.repository.PostRepository;
import project.DIY.service.PasswordUpdateService;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor
public class FollowController {	
	@Autowired
	private final MemberRepository memberRepository;
	@Autowired
	private final FollowRepository followRepository;
	
	@GetMapping("/follower/{id}")
	public String followerPage(@PathVariable("id") String id,HttpServletRequest req, Model model) {

		
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		Member user = memberRepository.selectBymemberId(Integer.parseInt(id));
		int memberId = member.getId();
		int userId = Integer.parseInt(id);

//		팔로잉/팔로워 수	
//		int following = followRepository.cntFollowee(member.getId());
//		int follower = followRepository.cntFollower(member.getId());
//		model.addAttribute("following",following);
//		model.addAttribute("follower",follower);
//		
		
		List<Follow> follower = followRepository.selectFollower(userId);
//		List<Follow> followeeList = followRepository.selectFollowee(userId);
		System.out.println(follower);
//		System.out.println(followeeList);

		List<Member> followerList = new ArrayList<>();
		
		for(int i=0; i<follower.size(); i++) {
			int temp = follower.get(i).getFollower();
			followerList.add(memberRepository.selectBymemberId(temp));
		};
		System.out.println(followerList);
		model.addAttribute("followerList",followerList);
		model.addAttribute("member", member);
		model.addAttribute("user", user);	
		
		return "follow/follower";
	}
	@GetMapping("/followee/{id}")
	public String followeePage(@PathVariable("id") String id,HttpServletRequest req, Model model) {

		
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		Member user = memberRepository.selectBymemberId(Integer.parseInt(id));
		int memberId = member.getId();
		int userId = Integer.parseInt(id);

//		팔로잉/팔로워 수	
//		int following = followRepository.cntFollowee(member.getId());
//		int follower = followRepository.cntFollower(member.getId());
//		model.addAttribute("following",following);
//		model.addAttribute("follower",follower);
//		
		

		List<Follow> followee = followRepository.selectFollowee(userId);

		System.out.println(followee);

		List<Member> followeeList = new ArrayList<>();
		
		for(int i=0; i<followee.size(); i++) {
			int temp = followee.get(i).getFollowee();
			followeeList.add(memberRepository.selectBymemberId(temp));
		};
		System.out.println(followeeList);
		model.addAttribute("followeeList",followeeList);
		
		
		model.addAttribute("member", member);
		model.addAttribute("user", user);	
		
		return "follow/followee";
	}

}
