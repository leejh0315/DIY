package project.DIY.repository.mybatis;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
import project.DIY.domain.ReReply;
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

	@Override
	public List<Reply> getReply(int postId) {
		List<Reply> reply = replyMapper.getReply(postId);
		return reply;
	}

	@Override
	public List<Member> selectNickname(int replyerId) {
		List<Member> nickAndSrc = replyMapper.selectNickname(replyerId);
		return nickAndSrc;
	}

	@Override
	public void insertReReply(Reply reply) {
		replyMapper.insertReReply(reply);
		
	}

	@Override
	public List<ReReply> selectReReplyById(int postId) {
		List<ReReply> reReply = replyMapper.selectReReplyById(postId);
		return reReply;
	}
	public void deleteReplybypostCode(int postId) {
		replyMapper.deleteReplybypostCode(postId);
		
	}
	
	
	
	
}
