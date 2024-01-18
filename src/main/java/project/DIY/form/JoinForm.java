package project.DIY.form;

import lombok.Data;

@Data
public class JoinForm {	//회원가입 유효성검사를 위한 폼
	private String loginId;
	private String password;
	private String passwordCheck;
	private String nickName;
	
	private boolean joongbokBool;
	private boolean emailConfirmBool;
}
