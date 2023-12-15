package project.DIY.repository.mybatis;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
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
	
}
