package project.DIY.form;

import lombok.Data;

@Data
public class PostForm {		//게시글 등록시, 유효성 검사를 위한 form
	private String postCtcode;
	private String targetName;
	
	private String targetSubName;	//대상 작품 부제목
	private String targetAuthor;	//대상 작품 작가		
	private String targetThumbnail;	
	private String targetGenre;
	
	private double starCount;		//작품에 남긴 별점
	
	private String title;
	private String content;
}