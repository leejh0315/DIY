package project.DIY.domain;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.Generated;

@Data
public class UploadFile {
	@Id @Generated
	private int fileId;
	private String fileName;               
	private String saveFileName;           
	private String filePath;               
	private String contentType;            
	private long size;                     
}
