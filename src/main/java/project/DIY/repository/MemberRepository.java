package project.DIY.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.DIY.domain.Member;

public interface MemberRepository {
	public Member selectById(String loginId);
	public boolean updateUUID(Member member);
	public Integer idCheck(String loginId);
	public void insertMember(Member member);
	public Member selectByCode(int code);
	public void updateById(Member member);
	public List<Map<String, String>> thisMonthWriteKing(int month);
	public List<Member> passwordUpdateSixMonth();
	public void reAlertUpdatePassword(int id);
	public void updatePasswordById(Member member);
	public List<Member> selectAllUser();
	public void updateUserStatusCode(HashMap<String, String> map);
	public Member selectBymemberId(int id);
	
}
