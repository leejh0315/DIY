package project.DIY.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import project.DIY.domain.Follow;



@Mapper
public interface FollowMapper {
	public void insertFollow(int follower, int followee);
	public List<Follow> selectFollowee(int memberId);
	public List<Follow> selectFollower(int memberId);
	public int followCheck(int memberId, int followee);
	public void unfollow(int memberId, int followee);
}
