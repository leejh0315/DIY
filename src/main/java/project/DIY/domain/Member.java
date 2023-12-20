package project.DIY.domain;

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
}


