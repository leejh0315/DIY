package project.DIY.domain;

import lombok.Data;

@Data
public class PasswordHistory {
	private int passwordHistoryId;
	private int memberId;
	private String password;
}
