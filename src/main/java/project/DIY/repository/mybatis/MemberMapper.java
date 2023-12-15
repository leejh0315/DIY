package project.DIY.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;

import project.DIY.domain.Member;

@Mapper
public interface MemberMapper {
	public Member selectById(String loginId);
	public void updateUUID(Member member);
}
