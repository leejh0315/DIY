package project.DIY.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.DIY.domain.Member;
import project.DIY.domain.PasswordHistory;

public interface MemberRepository {
	public Member selectById(String loginId); //loginId값에 해당하는 멤버 정보 가져옴
	public boolean updateUUID(Member member); //회원 UUID 변경
	public Integer idCheck(String loginId); //id 중복체크
	public void insertMember(Member member); //회원 추가
	public Member selectByCode(int code); //id값에 해당하는 멤버 정보 가져옴
	public void updateById(Member member); //id값에 해당하는 멤버 정보 수정
	public List<Map<String,String>> thisMonthWriteKing(int month); //해당 month에 가장 많이 기록한 회원 정보 가져옴
	public List<Member> passwordUpdateSixMonth(); //비밀번호 변경(6개월) 대상 멤버 정보 가져옴  
	public void reAlertUpdatePassword(int id); //비밀번호 변경 7일 후 다시 알림 설정 
	public void updatePasswordById(Member member); //해당id의 멤버 비밀번호 변경
	public List<Member> selectAllUser(); //전체 멤버 가져옴

	public void updateUserStatusCode(HashMap<String, String> map); //회원 활성화 및 비활성화 업데이트
	public Member selectBymemberId(int id); //id값에 해당하는 멤버 정보 가져옴
	public void updatePasswordByLoginId(Member member); //해당 loginId 멤버 비밀번호 변경
	public void insertPasswordHistory(PasswordHistory passwordHistory);
	public int selectCountByMemberIdAndPassword(PasswordHistory passwordHistory);
	public List<PasswordHistory> passwordHistoryByMemberId(int memberId);
	public List<Member> selectMemberBySearch(String search);
	public void deleteMemberById(String id);
	public void deletePostByMemberId(String id);
	public void deleteReplyByMemberId(String id);
	public void deleteReReplyByMemberId(String id);
	public void deleteChatRoomByMemberId(String id);
	public void deleteFollowByMemberId(String id);
	public void deleteLikePostByMemberId(String id);
	public void deleteNocieByMemberId(String id);
	public void deletePasswordHistoryByMemberId(String id);
	 
	 
}
