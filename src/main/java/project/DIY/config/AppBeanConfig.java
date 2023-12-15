package project.DIY.config;
import project.DIY.repository.*;
import project.DIY.repository.mybatis.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppBeanConfig {

	private final MemberMapper memberMapper;

	@Bean
	public MemberRepository memberRepository() {
		return new MybatisMemberRepository(memberMapper);
	}
}
