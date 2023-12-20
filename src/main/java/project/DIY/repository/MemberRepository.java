package project.DIY.repository;

import project.DIY.domain.Member;

public interface MemberRepository {
	public Member selectById(String loginId);
	public boolean updateUUID(Member member);
	public Integer idCheck(String loginId);
	public void insertMember(Member member);
	
}
