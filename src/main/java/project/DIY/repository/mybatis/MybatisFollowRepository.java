package project.DIY.repository.mybatis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.Follow;
import project.DIY.repository.FollowRepository;


@Repository
@RequiredArgsConstructor
@Primary
public class MybatisFollowRepository implements FollowRepository {
	@Autowired
	private final FollowMapper followMapper;

	@Override
	public void insertFollow(int follower, int followee) {
		followMapper.insertFollow(follower, followee);
		
	}
	//memberId인 사람이 팔로우하는 사람들 목록
	@Override
	public List<Follow> selectFollowee(int memberId) {
		List<Follow> followee = followMapper.selectFollowee(memberId);
		return followee;
	}
	//memberId인 사람을 팔로우하는 사람들 목록
	@Override
	public List<Follow> selectFollower(int memberId) {
		List<Follow> follower = followMapper.selectFollower(memberId);
		return follower;
	}
	
	//팔로우중복체크용
	@Override
	public int followCheck(int memberId, int followee) {
		int cnt = followMapper.followCheck(memberId, followee);
		return cnt;
	}
	@Override
	public void unfollow(int memberId, int followee) {
		followMapper.unfollow(memberId, followee);
		
	}
	@Override
	public int cntFollowee(int memberId) {
		int cnt = followMapper.cntFollowee(memberId);
		return cnt;
	}
	@Override
	public int cntFollower(int memberId) {
		int cnt = followMapper.cntFollower(memberId);
		return cnt;
	}

	
	
	

}
