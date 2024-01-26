package project.DIY.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReportPost {
	private int reportpostId;	//신고 id
	private int postCode; 		//대상 게시글 id
	private String title; 		//대상 게시글 제목
	private String content; 	//대상 게시글 내용
	private int memberId; 		//대상 게시글 작성자 id
	private int reporterId; 	//신고자 id
	private LocalDateTime reportedDate;	//신고 접수 날짜

}

