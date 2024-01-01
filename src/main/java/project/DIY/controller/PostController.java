package project.DIY.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.Post;
import project.DIY.repository.PostRepository;

@Controller
@RequiredArgsConstructor
public class PostController {
	private final PostRepository postRepository;
	
    @Value("${resource.handler}")
    private String resourceHandler;

    @Value("${resource.location}")
    private String resourceLocation;
	
	@GetMapping("/writePost")
	public String getPost(Model model) {
		model.addAttribute("writePost", new Post());
		return "post/writePost";
	}
	
    @PostMapping("/writePost")                                                        
    public String setArticle(@ModelAttribute Post post, Model model) {                
    	System.out.println(post);
    	//postRepository.insertPost(post);
    	//return "redirect:/article/" + post.getPostCode();
    	return "redirect:/";
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
}