package project.DIY.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import project.DIY.repository.AboutPostRepository;
import project.DIY.repository.MemberRepository;
import project.DIY.repository.PostRepository;
import project.DIY.repository.ReplyRepository;
import project.DIY.repository.mybatis.AboutPostMapper;
import project.DIY.repository.mybatis.MemberMapper;
import project.DIY.repository.mybatis.MybatisAboutPostRepository;
import project.DIY.repository.mybatis.MybatisMemberRepository;
import project.DIY.repository.mybatis.MybatisPostRepository;
import project.DIY.repository.mybatis.MybatisReplyRepository;
import project.DIY.repository.mybatis.PostMapper;
import project.DIY.repository.mybatis.ReplyMapper;

@Configuration
@RequiredArgsConstructor
public class AppBeanConfig {


   @Autowired
   private final MemberMapper memberMapper;
   @Autowired
   private final PostMapper postMapper;
   @Autowired
   private final ReplyMapper replyMapper;
   @Autowired
   private final AboutPostMapper aboutPostMapper;
   
   
   @Bean
   public MemberRepository memberRepository() {
      return new MybatisMemberRepository(memberMapper);
   }
   
   @Bean
   public PostRepository postRepository() {
      return new MybatisPostRepository(postMapper);
   }
   
   @Bean
   public ReplyRepository replyRepository() {
      return new MybatisReplyRepository(replyMapper);
   }
   @Bean
   public AboutPostRepository aboutPostRepository() {
	   return new MybatisAboutPostRepository(aboutPostMapper);
   }
   
   
   @Bean
   public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
       return restTemplateBuilder.build();
   }
   
}
