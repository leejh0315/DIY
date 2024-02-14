package project.DIY.repository.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
import project.DIY.domain.PasswordHistory;
import project.DIY.repository.MemberRepository;

@Repository
@RequiredArgsConstructor
@Primary
public class MybatisMemberRepository implements MemberRepository{

	@Autowired
	private final MemberMapper memberMapper;

	@Override
	public Member selectById(String loginId) {
		Member member = memberMapper.selectById(loginId);
		return member;
	}

	@Override
	public boolean updateUUID(Member member) {
		 
		boolean result = false;
		memberMapper.updateUUID(member);
		result = true;
		
		return result;
	}

	@Override
	public Integer idCheck(String loginId) {
		Integer cnt = memberMapper.idCheck(loginId);
		return cnt;
	}

	@Override
	public void insertMember(Member member) {
		memberMapper.insertMember(member);
	}

	@Override
	public Member selectByCode(int code) {
		Member member = memberMapper.selectByCode(code);
		return member;
	}

	@Override
	public void updateById(Member member) {
		memberMapper.updateById(member);
	}

	@Override
	public List<Map<String,String>>thisMonthWriteKing(int month) {
		List<Map<String,String>> post = memberMapper.thisMonthWriteKing(month);
		return post;
	}

	@Override
	public List<Member> passwordUpdateSixMonth() {
		List<Member> member = memberMapper.passwordUpdateSixMonth();
		return member;
	}

	@Override
	public void reAlertUpdatePassword(int id) {
		memberMapper.reAlertUpdatePassword(id);
		
	}

	@Override
	public void updatePasswordById(Member member) {
		memberMapper.updatePasswordById(member);
	}

	@Override
	public List<Member> selectAllUser() {
		List<Member> members =memberMapper.selectAllUser();
		return members;
	}

	@Override
	public void updateUserStatusCode(HashMap<String, String> map) {
		memberMapper.updateUserStatusCode(map);
	}

	@Override
	public Member selectBymemberId(int id) {
		Member members= memberMapper.selectBymemberId(id);
		return members;
	}

	@Override
	public void updatePasswordByLoginId(Member member) {
		memberMapper.updatePasswordByLoginId(member);
	}

	@Override
	public void insertPasswordHistory(PasswordHistory passwordHistory) {
		memberMapper.insertPasswordHistory(passwordHistory);
		
	}

	@Override
	public int selectCountByMemberIdAndPassword(PasswordHistory passwordHistory) {
		return memberMapper.selectCountByMemberIdAndPassword(passwordHistory);
	}

	@Override
	public List<PasswordHistory> passwordHistoryByMemberId(int memberId) {
		return memberMapper.passwordHistoryByMemberId(memberId);
	}

	@Override
	public void deleteMemberById(String id) {
		memberMapper.deleteMemberById(id);
		
	}

	@Override
	public void deletePostByMemberId(String id) {
		memberMapper.deletePostByMemberId(id);
	}

	@Override
	public void deleteReplyByMemberId(String id) {
		memberMapper.deleteReplyByMemberId(id);
	}

	@Override
	public void deleteReReplyByMemberId(String id) {
		memberMapper.deleteReReplyByMemberId(id);		
	}

	@Override
	public void deleteChatRoomByMemberId(String id) {
		memberMapper.deleteChatRoomByMemberId(id);
		
	}

	@Override
	public void deleteFollowByMemberId(String id) {
		memberMapper.deleteFollowByMemberId(id);
		
	}

	@Override
	public void deleteLikePostByMemberId(String id) {
		memberMapper.deleteLikePostByMemberId(id);		
	}

	@Override
	public void deleteNocieByMemberId(String id) {
		memberMapper.deleteNocieByMemberId(id);
	}

	@Override
	public void deletePasswordHistoryByMemberId(String id) {
		memberMapper.deletePasswordHistoryByMemberId(id);				
	}
	
}
