package project.DIY.domain;

import java.util.Date;

import lombok.Data;
@Data
public class Reply {
	private int replyId;			//댓글 id
	private int postId;				//대상 게시글 id
	private String replyContent;	//댓글 내용		
	private int replyerId;			//댓글 작성자 id
	private Date replyCreateDate;	//댓글 작성 날짜
	private Date replyUpdateDate;	//댓글 수정 날짜
	
	private String nickName;		//댓글 작성자 닉네임
	private String userProfileSrc;	//댓글 작성자 프로필
}
