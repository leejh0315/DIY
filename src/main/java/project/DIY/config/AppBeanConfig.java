package project.DIY.config;
import project.DIY.repository.*;
import project.DIY.repository.mybatis.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppBeanConfig {


	@Autowired
	private final MemberMapper memberMapper;
	@Autowired
	private final PostMapper postMapper;
	
	@Bean
	public MemberRepository memberRepository() {
		return new MybatisMemberRepository(memberMapper);
	}
	
	@Bean
	public PostRepository postRepository() {
		return new MybatisPostRepository(postMapper);
	}

	
	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}
	
}
