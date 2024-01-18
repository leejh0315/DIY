package project.DIY.domain;

import java.util.Date;

import lombok.Data;

@Data
public class ReReply {
	private int rereplyId;			//대댓글 id
	private int replyId;			//대상 댓글 id
	private String rereply;			//대댓글 내용
	private int rereplyerId;		//대댓글 작성자 id
	private Date rereplyCreateOn;	//대댓글 작성 날짜
	private Date rereplyUpdateOn;	//대댓글 수정 날짜
		
	private String nickName;		//대댓글 작성자 닉네임
	private String userProfileSrc;	//대댓글 작성자 프로필
}
