package project.DIY.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.UploadFile;
import project.DIY.repository.UploadFileRepository;

@Service
@RequiredArgsConstructor
public class ImageService {

	@Autowired
	private UploadFileRepository uploadFileRepository;
	private Path rootLocation; 
	
	
	// d:/image/

    @Autowired
    public ImageService(@Value("${upload.path}") String uploadPath) {
    	System.out.println("imageService 33번 줄");
        this.rootLocation = Paths.get(uploadPath);
        System.out.println(rootLocation.toString());
        //initialize();
    }
	public UploadFile store(MultipartFile file) throws Exception {             

		try {
			if(file.isEmpty()) {
				throw new Exception("Failed to store empty file " + file.getOriginalFilename());
			}
			System.out.println("imageSevice store 접근");
			String saveFileName = fileSave(rootLocation.toString(), file);     
			UploadFile saveFile = new UploadFile();
			System.out.println("imageSevice store 47번줄");
			saveFile.setFileName(file.getOriginalFilename()); System.out.println("file.getOriginalFilename()" + file.getOriginalFilename());
			saveFile.setSaveFileName(saveFileName); System.out.println("saveFileName" + saveFileName);
			saveFile.setContentType(file.getContentType());	System.out.println("file.getContentType()" + file.getContentType());
			saveFile.setSize(file.getResource().contentLength()); System.out.println("file.getResource().contentLength()" + file.getResource().contentLength());
			System.out.println("imageSevice store 52번줄");
			saveFile.setFilePath(rootLocation.toString().replace(File.separatorChar, '/') +'/' + saveFileName);
			System.out.println("imageSevice store 54번줄");
			System.out.println("saveFile.getFileId()" + saveFile.getFileId());
			int a = uploadFileRepository.save(saveFile);
			System.out.println("fileId = " + a);
			System.out.println("imageSevice store 56번줄");
			
			return saveFile;
			
		} catch(IOException e) {
			throw new Exception("Failed to store file " + file.getOriginalFilename(), e);
		}
		
		
	}

	public UploadFile load(String fileId) {
		return uploadFileRepository.findById(fileId);
				//.get();
	}
	
	public String fileSave(String rootLocation, MultipartFile file) throws IOException {
		File uploadDir = new File(rootLocation);
		
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		
		// saveFileName 생성
		UUID uuid = UUID.randomUUID();
		String saveFileName = uuid.toString() + file.getOriginalFilename();
		File saveFile = new File(rootLocation, saveFileName);
		FileCopyUtils.copy(file.getBytes(), saveFile);
		
		return saveFileName;
	}
}