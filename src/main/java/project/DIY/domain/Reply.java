package project.DIY.domain;

import java.util.Date;

import lombok.Data;
@Data
public class Reply {
	private int replyId;
	private int postId;
	private String replyContent;
	private int replyerId;
	private Date replyCreateDate;
	private Date replyUpdateDate;
}
