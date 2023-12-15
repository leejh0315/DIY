package project.DIY.domain;

import lombok.Data;

@Data
public class Member {
	private int id;
	private String loginId;
	private String passWord;
	private String userName;
	private String statusCode;
	private String birth;
	private String activeUUID; 
}
