package project.DIY.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Notice {
	private int id;
	private String type;
	private int targetId;
	private int targetMemberId;
	private int doMemberId;
	private int view;
	private LocalDateTime noticeOn;
	
	private String diff;
}
