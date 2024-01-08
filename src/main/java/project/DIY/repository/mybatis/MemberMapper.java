package project.DIY.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;

import project.DIY.domain.Member;
import project.DIY.form.JoinForm;

@Mapper
public interface MemberMapper {
	public Member selectById(String loginId);
	public void updateUUID(Member member);
	public Integer idCheck(String loginId);
	public void insertMember(Member member);
	public Member selectByCode(int code);
	public void updateById(Member member);
}
