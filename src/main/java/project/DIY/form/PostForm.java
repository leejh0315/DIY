package project.DIY.form;

import lombok.Data;

@Data
public class PostForm {
	private String postCtcode;
	private String targetName;
	
	private String targetSubName;	//대상 작품 부제목
	private String targetAuthor;	//대상 작품 작가		
	private String targetThumbnail;	
	private double starCount;		//작품에 남긴 별점
	
	private String title;
	private String content;
}