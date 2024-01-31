package project.DIY.controller;

import java.time.LocalDate;
import java.util.ArrayList;
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
import project.DIY.repository.FollowRepository;
import project.DIY.repository.MemberRepository;
import project.DIY.repository.PostRepository;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor
public class UserPageController { //userPage관련
	
	@Autowired
	private final PostRepository postRepository;
	@Autowired
	private final MemberRepository memberRepository;
	@Autowired
	private final FollowRepository followRepository;
	@Autowired
	private final AboutPostRepository aboutPostRepository;
	
	@GetMapping("/userPage/{id}")
	public String getUserPage(@PathVariable("id") String id,HttpServletRequest req, Model model,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "type", defaultValue = "all") String type
			)throws Exception {
		
		
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		aboutNotice(member, model);
		int memberId = member.getId();
		Member user = memberRepository.selectBymemberId(Integer.parseInt(id));
		int followingcheck = followRepository.followCheck(memberId, Integer.parseInt(id));
		model.addAttribute("followingcheck",followingcheck);
//		System.out.println(user);
		
		
//		팔로워/팔로우 수
		int following = followRepository.cntFollowee(Integer.parseInt(id));
		int follower = followRepository.cntFollower(Integer.parseInt(id));
		model.addAttribute("following",following);
		model.addAttribute("follower",follower);
		List<Post> userPost = postRepository.selectUserPostbyId(Integer.parseInt(id));
		int size = userPost.size();

		List<String> memberprofileimg =new ArrayList<>();
		 
		 for(int i = 0; i<userPost.size();i++) {
	    	  Integer userId =((Post) userPost.get(i)).getMemberId();
	    	  
	    	  
		   	  if(memberRepository.selectBymemberId(userId).getProfileSrc()==null) {
		   		  memberprofileimg.add("/img/defalut_profileimg.jpg");
		    	  }
		  	  else { memberprofileimg.add(memberRepository.selectBymemberId(userId).getProfileSrc());}
		    	 
	      }
		 model.addAttribute("profilesrc",memberprofileimg);

		LocalDate currentDate = LocalDate.now();
		int thisMonth = currentDate.getMonthValue();
		int thisyear = currentDate.getYear();
		
		
		LocalDate lastMonthDate = currentDate.minusMonths(1);
		int lastMonth = lastMonthDate.getMonthValue();
		int lastMonthyear = lastMonthDate.getYear();
		
		LocalDate last2MonthDate = currentDate.minusMonths(2);
		int last2Month = last2MonthDate.getMonthValue();
		int last2Monthyear = last2MonthDate.getYear();
		
		int[] dateArr = {last2Monthyear, lastMonthyear, thisyear, 
							last2Month, lastMonth, thisMonth};
		int[] cntArr = new int[6];
		for(int i=0;i<3;i++) {
			cntArr[i+3] = postRepository.countByMonth(Integer.parseInt(id), dateArr[i], dateArr[i+3]);
			cntArr[i] = dateArr[i+3];
		}
		
		model.addAttribute("cntArr", cntArr);
		
		PaginationVo paginationVo = new PaginationVo(userPost.size(), page);
		paginationVo.setMemberId(user.getId());
		paginationVo.setType(type);
		int cnt = postRepository.getPostsCountByMemberId(paginationVo);

		paginationVo.setOffset((page-1)*5);
		
		List<Post> list = postRepository.getPostsByPageByMemberId(paginationVo);
		
		for(int i=0; i<list.size();i++) {
			String contentTemp = list.get(i).getContent();
			String plainText = contentTemp.replaceAll("\\<.*?\\>", "");;
			plainText = plainText.replaceAll("&nbsp;", "");
			plainText = plainText.replaceAll("&gt;", "");
			list.get(i).setContent(plainText);
		}
		
		int endPage = (cnt/5 <= 0)? 1 :(int)(Math.ceil(cnt/5.0));  
		paginationVo.setEndPage(endPage);

		model.addAttribute("size",size);
		model.addAttribute("type", type);
	    model.addAttribute("page", page);
	    model.addAttribute("pageVo", paginationVo);
	    model.addAttribute("posts", userPost);
		model.addAttribute("post",list);
		model.addAttribute("member", member);
		model.addAttribute("user", user);	
		

		return "myPage/userPage";
	}
	
	@PostMapping("/insertFollow") //팔로잉 기능
	@ResponseBody
	public String insertFollow(@RequestParam(value = "followee") int followee ,  HttpServletRequest req) {
		
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		int memberId = member.getId();
		System.out.println(memberId);
		int cnt = followRepository.followCheck(memberId, followee);
		
		if (cnt == 0) {
			//팔로잉중 아니면 팔로우
			followRepository.insertFollow(memberId, followee);
			return "1";
		} else {
			//이미 팔로잉중이면 언팔로우
			followRepository.unfollow(memberId, followee);
			return "0";
		}
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
