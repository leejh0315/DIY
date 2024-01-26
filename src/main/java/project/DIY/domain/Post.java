package project.DIY.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Post {
	private String postCode;
	private String postCtcode; //카테고리 : 영화, 책, 공연
	
	private String targetName;	//대상 작품 이름
	private String targetSubName;	//대상 작품 부제목
	private String targetAuthor;	//대상 작품 작가		
	private String targetThumbnail;	//대상 작품 썸네일
	private String targetGenre;		//대상 작품 장르
	
	private int memberId;		//회원 id		
	private String memberNick;	//회원 닉네임
	private double starCount;		//작품에 남긴 별점
	private String title;		//게시글 제목
	
	
	private String content;		//게시글 내용
	private LocalDateTime createOn;		//게시글 작성 날짜
	private LocalDateTime updateOn;		//게시글 수정 날짜
	
	private int views;			//게시글 조회수
}
