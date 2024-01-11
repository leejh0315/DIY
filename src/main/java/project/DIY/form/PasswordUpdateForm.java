package project.DIY.form;

import lombok.Data;

@Data
public class PasswordUpdateForm {
	private String originPassword;
	private String newPassword;
	private String newPasswordCheck;
	private String type;
}
