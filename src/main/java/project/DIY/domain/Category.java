package project.DIY.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {
	private String type;		//카테고리 타입
	private String displayName;	//카테고리 이름
}
