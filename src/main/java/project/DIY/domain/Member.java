package project.DIY.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Member {
	private int id;
	private String loginId;
	private String password;
	private String nickName;
	private String statusCode;
	private String birth;
	private String activeUUID;
	private Date createOn;
	private Date updateOn;
	private String profileSrc;
	private String memberIntro;
	private Date passwordUpdate;
	private String passwordFind;
}


