package project.DIY.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Follow {
	private int follower;	//팔로우한 사람
	private int followee;	//팔로잉한 사람
}
