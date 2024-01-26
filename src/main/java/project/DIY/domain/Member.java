package project.DIY.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Member {
	private int id;				//id
	private String loginId;		//email
	private String password;	//비밀번호
	private String nickName;	//닉네임		
	private String statusCode;	//활동상태(Y,N)
	private String activeUUID;	//세션등록된 UUID
	private LocalDateTime createOn;		//생성날짜
	private LocalDateTime updateOn;		//회원정보 수정날짜
	private String profileSrc;	//프로필사진 경로
	private String memberIntro;	//한줄소개
	private LocalDateTime passwordUpdate;//비밀번호 수정날짜
	private String passwordFind;//비밀번호 찾기 여부(Y,N)
}


