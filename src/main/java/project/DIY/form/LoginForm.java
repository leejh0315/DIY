package project.DIY.form;

import lombok.Data;

@Data
public class LoginForm {	//로그인 유효성검사를 위한 form
	private String loginId;
	private String passWord;
}