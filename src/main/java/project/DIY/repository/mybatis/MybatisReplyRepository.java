package project.DIY.repository.mybatis;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.Reply;
import project.DIY.repository.ReplyRepository;
@Repository
@RequiredArgsConstructor
@Primary
public class MybatisReplyRepository implements ReplyRepository{
	
	
	private final ReplyMapper replyMapper;
	
	@Override
	public void insertReply(Reply reply) {
		replyMapper.insertReply(reply);
		
	}
	
}
