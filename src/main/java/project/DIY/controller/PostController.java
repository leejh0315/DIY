package project.DIY.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.Category;
import project.DIY.domain.Likes;
import project.DIY.domain.Member;
import project.DIY.domain.Post;
import project.DIY.domain.ReReply;
import project.DIY.domain.Reply;
import project.DIY.domain.ReportPost;
import project.DIY.form.PostForm;
import project.DIY.repository.AboutPostRepository;
import project.DIY.repository.MemberRepository;
import project.DIY.repository.PostRepository;
import project.DIY.repository.ReplyRepository;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor
public class PostController {
	
	@Autowired
	private final PostRepository postRepository;
	@Autowired
	private final MemberRepository memberRepository;
	@Autowired
	private final ReplyRepository replyRepository;
	@Autowired
	private final AboutPostRepository aboutPostRepository;
	
	//게시글 작성 페이지
	@GetMapping("/writePost")
	public String getPost(Model model, HttpServletRequest req, PostForm postForm) {
		HttpSession session = req.getSession();
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);

		model.addAttribute("member", member);
		
		List<Category> ct = new ArrayList<>();
		ct.add(new Category("book", "책"));
		ct.add(new Category("movie", "영화"));
		ct.add(new Category("concert", "공연"));
	
		model.addAttribute("postForm", postForm);
		model.addAttribute("writePost", new Post());
		model.addAttribute("ct", ct);
		return "post/writePost";
	}

    // 서머노트 이미지 업로드 temp 저장
    @RequestMapping(value = "/writePost/uploadSummernoteImageFile", produces = "application/json; charset=utf8")
    @ResponseBody
    public String uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile,
            HttpServletRequest request) {
        
    	// JSON 객체 생성
        JsonObject jsonObject = new JsonObject();
        // 이미지 파일이 저장될 경로 설정
        //String fileRoot  = "C:\\image\\temp\\"; 
        String fileRoot  = "C:\\DIY\\src\\main\\resources\\static\\image\\post\\";
        // 업로드된 파일의 원본 파일명과 확장자 추출
        String originalFileName = multipartFile.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // 새로운 파일명 생성 (고유한 식별자 + 확장자)
        String savedFileName = UUID.randomUUID() + extension;
        // 저장될 파일의 경로와 파일명을 나타내는 File 객체 생성
        File targetFile = new File(fileRoot + savedFileName);

        try {
            // 업로드된 파일의 InputStream 얻기
            java.io.InputStream fileStream = multipartFile.getInputStream();
            // 업로드된 파일을 지정된 경로에 저장
            FileUtils.copyInputStreamToFile(fileStream, targetFile);

            // JSON 객체에 이미지 URL과 응답 코드 추가
            //jsonObject.addProperty("src", "/temp/" + savedFileName);
            jsonObject.addProperty("src", "/image/post/" + savedFileName);
            jsonObject.addProperty("responseCode", "success");
        } catch (IOException e) {
            // 파일 저장 중 오류가 발생한 경우 해당 파일 삭제 및 에러 응답 코드 추가
            FileUtils.deleteQuietly(targetFile);
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }
        // JSON 객체를 문자열로 변환하여 반환
        String a = jsonObject.toString();
    	
        return a;
    }
    
    //서머노트 이미지 삭제
	@RequestMapping(value = "/deleteSummernoteImageFile", produces = "application/json; charset=utf8")
	@ResponseBody
	public void deleteSummernoteImageFile(@RequestParam("file") String fileName) {
		String filePath = "C:\\image\\temp\\";;
		deleteFile(filePath, fileName);
	}
    
   public void validateJoinForm(PostForm postForm, Errors errors) {
	    if (!StringUtils.hasText(postForm.getTargetName())) {
	        errors.rejectValue("targetName", null, "작품을 입력해주세요.");
	    } 
	    if (!StringUtils.hasText(postForm.getTitle())) {
	        errors.rejectValue("title", null, "게시글 제목을 입력해주세요.");
	    } 
	    if (!StringUtils.hasText(postForm.getContent())) {
	        errors.rejectValue("content", null, "게시글 내용을 입력해주세요.");
	    }
   }
	   
    //게시글 작성, 유효성 검사 이후 DB 입력
	@PostMapping("/writePost")                                                        
    public String setArticle(@ModelAttribute Post post, @ModelAttribute PostForm postForm, 
    		Model model, HttpServletRequest req, BindingResult bindingResult) {
		
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		List<Category> ct = new ArrayList<>();
		ct.add(new Category("book", "책"));
		ct.add(new Category("movie", "영화"));
		ct.add(new Category("concert", "공연"));
		
		validateJoinForm(postForm, bindingResult);
		
	    if(bindingResult.hasErrors()) {
    	  System.out.println(postForm);
    	  model.addAttribute("postForm", postForm);
    	  model.addAttribute("member", member);
    	  model.addAttribute("ct", ct);
    	  return "post/writePost";
	    }
	    else{
    	LocalDateTime currentDateTime = LocalDateTime.now();
    	post.setMemberId(member.getId());
  		post.setMemberNick(member.getNickName());
  		post.setCreateOn(currentDateTime);
  		System.out.println(post.getContent().length());
  		post.setTargetName(post.getTargetName().trim());
      	
      	postRepository.insertPost(post);
      	String postCode = postRepository.getLastPost(member.getId());

      	return "redirect:/posts/" + postCode;
       }
    }
	//게시글 삭제
	@PostMapping("/postdelete")
	@ResponseBody
	public String deletePostByPostId(@RequestParam(value = "previousPage") String previousPage
			, @RequestParam(value = "postCode") String postCode,
			
			
			 HttpServletRequest req) {
		System.out.println("접근");
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		/*
		
		postRepository.deletePost(postCode);
		
		aboutPostRepository.deletePostLikes(postCode);
		aboutPostRepository.deleteReportPost(postCode);
		replyRepository.deleteReplybypostCode(postCode);
		*/
		System.out.println(previousPage);
		
	
		if(previousPage.contains("myPage")) {
			System.out.println("myPage");
			return "myPage";
		}else {
			if(previousPage.contains("book")) {
				System.out.println("book");
				return "book";
			}else if(previousPage.contains("movie")) {
				System.out.println("movie");
				return "movie";
			}
			else {
				System.out.println("concert");
				return "concert";
			}
		}
		
//		previousPage=previousPage.substring(a.length());
//		System.out.println(previousPage);
		
	}
	
	//게시글 상세보기
	@GetMapping("/posts/{postCode}")
	public String getPostByPostId(@ModelAttribute Reply reply, Model model,
			@PathVariable("postCode") int postCode, HttpServletRequest req) {

		HttpSession session = req.getSession(false);
		
		Post postItem = postRepository.selectByPostCode(postCode);
		int postWriteMemberCode = postItem.getMemberId();
		Member postWriteMember = memberRepository.selectByCode(postWriteMemberCode);
		List<Reply> r = replyRepository.getReply(postCode);
		
		if(session == null) {
			Member member = new Member();
			model.addAttribute("member", member);
		}else {
			Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
			if(member != null) {
				Likes likes = new Likes();
				ReportPost reportPost = new ReportPost();
				likes.setMemberId(member.getId());
				likes.setPostCode(postCode);
				reportPost.setPostCode(postCode);
				reportPost.setTitle(postItem.getTitle());
				reportPost.setContent(postItem.getContent());
				reportPost.setMemberId(postItem.getMemberId());
				reportPost.setReporterId(member.getId());
				int likescnt = aboutPostRepository.selectLikes(likes);
				System.out.println("likesCnt" + likescnt); 
				
				int reportpostcnt = aboutPostRepository.selectReportPost(reportPost);
				model.addAttribute("likescnt",likescnt);
				model.addAttribute("reportpostcnt",reportpostcnt);
			}
			
			model.addAttribute("member", member);
		}
		
		postItem = postRepository.selectByPostCode(postCode);
		Map<Integer, Object> reRep = new HashMap<Integer ,Object>();
			for(int i =0; i<r.size();i++) {
				System.out.println(r.get(i).getReplyId());
				List<ReReply> reReply = replyRepository.selectReReplyById(r.get(i).getReplyId());
				
				for(int j = 0 ; j < reReply.size();j++) {
					List<Member> m = replyRepository.selectNickname(reReply.get(j).getRereplyerId());							
					reReply.get(j).setNickName(m.get(0).getNickName());
					reReply.get(j).setUserProfileSrc(m.get(0).getProfileSrc());
					reRep.put(r.get(i).getReplyId(), reReply);
				}
				
				List<Member> nickAndSrc = replyRepository.selectNickname(r.get(i).getReplyerId());
				r.get(i).setNickName(nickAndSrc.get(0).getNickName());
				r.get(i).setUserProfileSrc((nickAndSrc.get(0).getProfileSrc()));
			};
		
		model.addAttribute("reRep", reRep);
		model.addAttribute("post",postItem);
		model.addAttribute("postCode", postCode);
		model.addAttribute("reply",reply);
		model.addAttribute("postWriteMember", postWriteMember);
		model.addAttribute("replylist",r);
			
		return "post/post";
	}
	
	//게시글 업데이트 페이지
	@GetMapping("/update/{postCode}")
	public String getUpdatePostByPostId(Model model, @PathVariable("postCode") int postCode, HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		Post post = postRepository.selectByPostCode(postCode);
		
		if(member.getId() != post.getMemberId()) {
			return "redirect:/home/home";
		}
		model.addAttribute("member", member);
		model.addAttribute("post", post);
		
		return "post/updatePost";
	}
	
	//게시글 업데이트 DB update
	@PostMapping("/update/{postCode}")
	public String postUpdatePostByPostId(@ModelAttribute Post post, @PathVariable("postCode") int postCode, HttpServletRequest req) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		post.setUpdateOn(currentDateTime);
		postRepository.updatePostByPostCode(post);
		return "redirect:/posts/{postCode}";
	}
	
	//파일 지우는 메소드
	private void deleteFile(String filePath, String fileName) {
		Path path = Paths.get(filePath, fileName);
		System.out.println("Path : "+ path);
		try {
			Files.delete(path);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}