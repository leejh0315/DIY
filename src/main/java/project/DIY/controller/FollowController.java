package project.DIY.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.ChatRoom;
import project.DIY.domain.Follow;
import project.DIY.domain.Member;
import project.DIY.domain.Notice;
import project.DIY.repository.AboutPostRepository;
import project.DIY.repository.ChatRepository;
import project.DIY.repository.FollowRepository;
import project.DIY.repository.MemberRepository;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor
public class FollowController {	
	
	
	@Autowired
	private final MemberRepository memberRepository;
	@Autowired
	private final FollowRepository followRepository;
	@Autowired
	private final ChatRepository chatRepository;
	@Autowired
	private final AboutPostRepository aboutPostRepository;
	
	@GetMapping("/follower/{id}")
	public String followerPage(@PathVariable("id") int id,HttpServletRequest req, Model model) {

		
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		aboutNotice(member, model);
		Member user = memberRepository.selectBymemberId(id);
		int memberId = member.getId();
		int userId = id;

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
		List<Integer> followCheckList = new ArrayList<>();
		for(int i=0; i<follower.size(); i++) {
			int temp = follower.get(i).getFollower();
			followerList.add(memberRepository.selectBymemberId(temp));
			
			if(temp == memberId) {
				followCheckList.add(2);
			} else {
				int followCheck = followRepository.followCheck(memberId, temp);
				followCheckList.add(followCheck);
			};
		};
		
		List<ChatRoom> cr = chatRepository.selectMyRoom(id);
		System.out.println("cr: " + cr);
		
		Map<Integer, String> chatMap = new HashMap<Integer, String>();
		
		for(int i =0; i<cr.size(); i++) {
			
			if(cr.get(i).getChatReceiverId() == id) {
				for(int j =0;j <follower.size(); j++) {
					if(cr.get(i).getChatSenderId() == follower.get(j).getFollower()) {
						chatMap.put(follower.get(j).getFollower(), cr.get(i).getRoomId());
					}
				}
			}else if(cr.get(i).getChatSenderId() == id) {
				for(int j =0;j <follower.size(); j++) {
					if(cr.get(i).getChatReceiverId() == follower.get(j).getFollower()) {
						chatMap.put(follower.get(j).getFollower(), cr.get(i).getRoomId());
					}
				}
			}
			
		}
		
		System.out.println(followerList);
		System.out.println(followCheckList);
		model.addAttribute("followerList",followerList);
		model.addAttribute("followCheckList",followCheckList);
		model.addAttribute("member", member);
		model.addAttribute("user", user);
		model.addAttribute("chatMap", chatMap);
//		model.addAttribute("followCheck",followCheck);
		return "follow/follower";
	}
	@GetMapping("/followee/{id}")
	public String followeePage(@PathVariable("id") int id,HttpServletRequest req, Model model) {

		
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		aboutNotice(member, model);
		Member user = memberRepository.selectBymemberId(id);
		int memberId = member.getId();
		int userId = id;

		
//		팔로잉/팔로워 수	
//		int following = followRepository.cntFollowee(member.getId());
//		int follower = followRepository.cntFollower(member.getId());
//		model.addAttribute("following",following);
//		model.addAttribute("follower",follower);
//		
		

		List<Follow> followee = followRepository.selectFollowee(userId);

		System.out.println(followee);

		
		List<Member> followeeList = new ArrayList<>();
		List<Integer> followCheckList = new ArrayList<>();
		
		for(int i=0; i<followee.size(); i++) {
			int temp = followee.get(i).getFollowee();
			followeeList.add(memberRepository.selectBymemberId(temp));
			if(temp == memberId) {
				followCheckList.add(2);
			} else {
				int followCheck = followRepository.followCheck(memberId, temp);
				followCheckList.add(followCheck);
			};
		};
		System.out.println(followeeList);
		
		List<ChatRoom> cr = chatRepository.selectMyRoom(id);
		System.out.println("cr: " + cr);
		
		Map<Integer, String> chatMap = new HashMap<Integer, String>();
		
		for(int i =0; i<cr.size(); i++) {
			
			if(cr.get(i).getChatReceiverId() == id) {
				for(int j =0;j <followee.size(); j++) {
					if(cr.get(i).getChatSenderId() == followee.get(j).getFollowee()) {
						chatMap.put(followee.get(j).getFollowee(), cr.get(i).getRoomId());
					}
				}
			}else if(cr.get(i).getChatSenderId() == id) {
				for(int j =0;j <followee.size(); j++) {
					if(cr.get(i).getChatReceiverId() == followee.get(j).getFollowee()) {
						chatMap.put(followee.get(j).getFollowee(), cr.get(i).getRoomId());
					}
				}
			}
			
		}
		System.out.println(chatMap);
		
		
		model.addAttribute("followeeList",followeeList);
		model.addAttribute("followCheckList",followCheckList);
		
		
		model.addAttribute("member", member);
		model.addAttribute("user", user);	
		
		model.addAttribute("chatMap", chatMap);
		
		return "follow/followee";
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
