package project.DIY.repository;

import java.util.List;

import project.DIY.domain.Follow;

public interface FollowRepository {
	public void insertFollow(int follower, int followee);
	public List<Follow> selectFollowee(int memberId);
	public List<Follow> selectFollower(int memberId);
	public int followCheck(int memberId, int followee);
	public void unfollow(int memberId, int followee);
	public int cntFollowee (int memberId);
	public int cntFollower (int memberId);

}
