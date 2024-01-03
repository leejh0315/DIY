package project.DIY.controller;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import project.DIY.domain.Member;
import project.DIY.domain.Post;
import project.DIY.repository.MemberRepository;
import project.DIY.repository.PostRepository;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor
public class PostController {
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	
    @Value("${resource.handler}")
    private String resourceHandler;

    @Value("${resource.location}")
    private String resourceLocation;
	
	@GetMapping("/writePost")
	public String getPost(Model model, HttpServletRequest req) {
		HttpSession session = req.getSession();
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);

		model.addAttribute("member", member);
		
		List<Category> ct = new ArrayList<>();
		ct.add(new Category("book", "책"));
		ct.add(new Category("concert", "공연"));
		ct.add(new Category("movie", "영화"));
	
		model.addAttribute("writePost", new Post());
		model.addAttribute("ct", ct);
		return "post/writePost";
	}
	
	
	@GetMapping("/selectPost")
	public String selectPost(Model model) {
		Post post = postRepository.selectPost();
		model.addAttribute("post", post);
		return "post/selectPost";
	}


    // 서머노트 이미지 업로드 temp 저장
    @RequestMapping(value = "/writePost/uploadSummernoteImageFile", produces = "application/json; charset=utf8")
    @ResponseBody
    public String uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile,
            HttpServletRequest request) {
        
    	System.out.println("uploadSummernoteImageFile Post 요청 접근 완료");
    	// JSON 객체 생성
        JsonObject jsonObject = new JsonObject();

        // 이미지 파일이 저장될 경로 설정
        String fileRoot  = "C:\\image\\temp\\"; 

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
            jsonObject.addProperty("src", "/temp/" + savedFileName);
            jsonObject.addProperty("responseCode", "success");
        } catch (IOException e) {
            // 파일 저장 중 오류가 발생한 경우 해당 파일 삭제 및 에러 응답 코드 추가
            FileUtils.deleteQuietly(targetFile);
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }

        // JSON 객체를 문자열로 변환하여 반환
        String a = jsonObject.toString();
        System.out.println(a);
    	System.out.println("uploadSummernoteImageFile Post 요청 수행 완료");
        return a;
    }
    
	@RequestMapping(value = "/deleteSummernoteImageFile", produces = "application/json; charset=utf8")
	@ResponseBody
	public void deleteSummernoteImageFile(@RequestParam("file") String fileName) {
		String filePath = "C:\\image\\temp\\";;
		deleteFile(filePath, fileName);
	}
    

	@PostMapping("/writePost")                                                        
    public String setArticle(@ModelAttribute Post post, Model model) {                
    	System.out.println(post);
    	System.out.println("writePost Post요청 접근");
    	
    	postRepository.insertPost(post);
    	//return "redirect:/article/" + post.getPostCode();
    	return "redirect:/";
    }
	
	@GetMapping("/posts/{postCode}")
	public String getPostByPostId(Model model, @PathVariable("postCode") int postCode, @ModelAttribute("post") Post postItem
			, HttpServletRequest req) {
		
		postItem = postRepository.selectByPostCode(postCode);
		int postWriteMemberCode = postItem.getMemberId();
		Member postWriteMember = memberRepository.selectByCode(postWriteMemberCode);
		
		String targetTumb = postItem.getTargetThumbnail();
		
		System.out.println(postItem);
		System.out.println(postWriteMember);
		HttpSession session = req.getSession();
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);

		model.addAttribute("post",postItem);
		model.addAttribute("postWriteMember", postWriteMember);
		model.addAttribute("targetTumb", targetTumb);
		model.addAttribute("member", member);
		
		
		return "post/post";
	}
	
	
//	@PostMapping("/writePost")
//	public String postPost(@ModelAttribute Post post, HttpServletRequest req) {
//		
//		
//		System.out.println(post);
//		
//		//HttpSession session = req.getSession(false);
//		//Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
//		//System.out.println(member);
//		//postRepository.insertPost(post);
//		return "redirect:/";
//	}
	
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