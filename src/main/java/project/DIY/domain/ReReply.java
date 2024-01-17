package project.DIY.domain;

import java.util.Date;

import lombok.Data;

@Data
public class ReReply {
	private int rereplyId;
	private int replyId;
	private String rereply;
	private int rereplyerId;
	private Date rereplyCreateOn;
	private Date rereplyUpdateOn;
	
	private String nickName;
	private String userProfileSrc;
}
