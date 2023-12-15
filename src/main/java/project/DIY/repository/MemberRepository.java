package project.DIY.repository;

import project.DIY.domain.Member;

public interface MemberRepository {
	public Member selectById(String loginId);
	public boolean updateUUID(Member member);
}
