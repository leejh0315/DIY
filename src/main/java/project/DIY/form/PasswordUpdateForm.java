package project.DIY.form;

import lombok.Data;

@Data
public class PasswordUpdateForm {	//비밀번호 변경 유효성검사를 위한 form
	private String originPassword;
	private String newPassword;
	private String newPasswordCheck;
	private String type;
}
