package project.DIY.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
import project.DIY.domain.Notice;
import project.DIY.domain.Post;
import project.DIY.domain.Reply;
import project.DIY.domain.ReportPost;
import project.DIY.repository.AboutPostRepository;
import project.DIY.repository.MemberRepository;
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
	@Autowired
	private final MemberRepository memberRepository;
	
	
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
	          System.out.println(chatCnt);
	          System.out.println(noticeCnt);
	      }
	   
 	}
	
	@GetMapping("/notice/{id}")
	public String getNotice(Model model, HttpServletRequest req, @PathVariable("id") int id) {
		HttpSession session = req.getSession();
	    Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
	    
	    LocalDateTime currentDateTime = LocalDateTime.now();
		
	    List<Notice> noticeList = aboutPostRepository.selectNoticeById(member.getId());
	    
	   
	    Map<Integer, Member> doMember = new HashMap<Integer,Member>();
	    Map<Integer, Post> targetPost = new HashMap<Integer, Post>();
	    for(int i =0;i<noticeList.size();i++) {
	    	Member tempMember = memberRepository.selectBymemberId(noticeList.get(i).getDoMemberId());
	    	doMember.put(tempMember.getId(), tempMember);
	    	
	    	Duration duration = Duration.between(noticeList.get(i).getNoticeOn(), currentDateTime);
	    	 long hours = duration.toHours();
	         long minutes = duration.toMinutes() % 60;
	         System.out.println("두 시간의 차이: " + hours + " 시간 " + minutes + " 분");
	         String diff = "";
	         if(hours == 0) {
	        	 diff = minutes + "분 전";
	        	
	         }else if(hours > 0 && hours < 24) {
	        	 diff = hours + "시간 전";
	         }
	         else if(hours >= 24) {
	        	 diff = (hours/24) + "일 전";
	        	 if(hours/24 > 30) {
	        		 diff = ((hours/24)/30) + "달 전";
	        	 }
	         }
	         noticeList.get(i).setDiff(diff);
	         
	         aboutPostRepository.updateNoticeView(1, id);
	         if(!noticeList.get(i).getType().equals("chat")) {
	        	 
	        	 if(noticeList.get(i).getType().equals("reply")) {
		        	 noticeList.get(i).setType("댓글을 작성하셨습니다.");
		         }else if(noticeList.get(i).getType().equals("like")) {
		        	 noticeList.get(i).setType("좋아요를 눌렀습니다.");
		         }
	         }
	         
	         
	         if(!noticeList.get(i).getType().equals("chat")) {
	        	 Post tempPost = postRepository.selectByPostCode(noticeList.get(i).getTargetId());
	        	 targetPost.put(noticeList.get(i).getTargetId(), tempPost);
	         }
	    }
	    System.out.println(targetPost);
	    System.out.println(noticeList);
	    int chatCnt = 0;
	    int noticeCnt = 0;
	    int noticeListLen = noticeList.size();
	    model.addAttribute("chatCnt", chatCnt);
        model.addAttribute("noticeCnt", noticeCnt); 
	    model.addAttribute("targetPost", targetPost);
	    model.addAttribute("member", member);
		model.addAttribute("noticeList", noticeList);
		model.addAttribute("noticeListLen", noticeListLen);
		model.addAttribute("doMember", doMember);
		
		return "myPage/notice";
		
	}
	
	
	@PostMapping("/insertLike")
	@ResponseBody
	public String insertLike(@RequestParam(value = "postCode") int postCode , HttpServletRequest req) {
		
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		Post post = postRepository.selectByPostCode(postCode);
		Likes likes = new Likes();
		int userid = member.getId();
		likes.setMemberId(userid);
		likes.setPostCode(postCode);
		
		int cnt = aboutPostRepository.selectLikes(likes);
		System.out.println("이미 들어가 있는 갯수 cnt : " + cnt);
		
		LocalDateTime currentDateTime = LocalDateTime.now();
		Notice notice = new Notice();
		notice.setType("like");
		notice.setTargetId(postCode);
		notice.setDoMemberId(member.getId());
		notice.setTargetMemberId(post.getMemberId());
		notice.setNoticeOn(currentDateTime);
		if (cnt ==0) {
			aboutPostRepository.insertLikes(likes);
			aboutPostRepository.insertNotice(notice);
			return "1";
		}else {
			//likes 가 삭제되어야 한다.
			aboutPostRepository.deleteLikes(likes);
			aboutPostRepository.deleteNoticeLike(notice);
			return "0";
		}
	}
	
	@PostMapping("/reReplyPost/{postCode}")	//대댓
	public String insertReReplyPost( @PathVariable("postCode") String postCode,
			@ModelAttribute Reply reply, HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		reply.setReplyerId(member.getId());
		System.out.println("대댓");
		System.out.println(reply);
		LocalDateTime currentDateTime = LocalDateTime.now();
		reply.setReplyCreateDate(currentDateTime);
		replyRepository.insertReReply(reply);
		
		return "redirect:/posts/{postCode}";
	}
	
	
	@PostMapping("/insertReportPost")
	@ResponseBody
	public String insertReportPost(@RequestParam(value = "postCode") int postCode, HttpServletRequest req) {
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
		LocalDateTime currentDateTime = LocalDateTime.now();
		reportPost.setReportedDate(currentDateTime);
		System.out.println(reportPost);
		
		int selectReportPostcnt = aboutPostRepository.selectReportPost(reportPost);
		System.out.println("----------------------");
		System.out.println(selectReportPostcnt);
		System.out.println("----------------------");
		if (selectReportPostcnt == 0) {
			aboutPostRepository.insertReportPost(reportPost);
			return "1";
		}else {
			return "0";
		}
	}
	
	@PostMapping("/deleteReportPost")
	@ResponseBody
	public String deleteReportPost(@RequestParam(value = "postCode") int postCode, HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		Post currentPost = postRepository.selectByPostCode(postCode);
		int userid = member.getId();
		
		
		aboutPostRepository.deleteReportPost(postCode);
		
		return "1";
	}
	
	
}
