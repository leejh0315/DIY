package project.DIY.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.UploadFile;
import project.DIY.service.ImageService;

@Controller
@RequiredArgsConstructor
public class ImageController {

    @Autowired
    private final ImageService imageService;

    @Autowired
    private final ResourceLoader resourceLoader;

    @ResponseBody
    @PostMapping("/image")
    public ResponseEntity<?> imageUpload(@RequestParam("file") MultipartFile file) {
        System.out.println("img접근 하였는가");

        try {
            UploadFile uploadFile = imageService.store(file);
            System.out.println("imageservice.store실행완료.");
            System.out.println(uploadFile.getFileId());
            
            // fileId를 반환
            return ResponseEntity.ok().body(uploadFile.getFileId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    @ResponseBody
    @GetMapping("/image/{fileId}")
    public ResponseEntity<?> serveFile(@PathVariable("fileId") Long fileId) {
        System.out.println("imageController fileId =" + fileId);
        
        try {
            // fileId를 사용하여 이미지를 조회
            UploadFile uploadFile = imageService.load(String.valueOf(fileId));
            System.out.println(uploadFile);
            Resource resource = resourceLoader.getResource("file:" + uploadFile.getFilePath());
            return ResponseEntity.ok().body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}

