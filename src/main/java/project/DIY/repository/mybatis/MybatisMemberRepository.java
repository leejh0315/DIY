package project.DIY.repository.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
import project.DIY.domain.Post;
import project.DIY.repository.MemberRepository;

@Repository
@RequiredArgsConstructor
@Primary
public class MybatisMemberRepository implements MemberRepository{

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
	
}
