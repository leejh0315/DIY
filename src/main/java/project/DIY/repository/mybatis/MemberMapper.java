package project.DIY.repository.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import project.DIY.domain.Member;

@Mapper
public interface MemberMapper {
	public Member selectById(String loginId);
	public void updateUUID(Member member);
	public Integer idCheck(String loginId);
	public void insertMember(Member member);
	public Member selectByCode(int code);
	public void updateById(Member member);
	public List<Map<String,String>> thisMonthWriteKing(int month);
	public List<Member> passwordUpdateSixMonth();
	public void reAlertUpdatePassword(int id);
	public void updatePasswordById(Member member);
	public List<Member> selectAllUser();

	public void updateUserStatusCode(HashMap<String, String> map);
}
