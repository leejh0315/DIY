package project.DIY.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import io.lettuce.core.dynamic.annotation.Param;
import project.DIY.domain.Member;
import project.DIY.domain.ReReply;
import project.DIY.domain.Reply;

@Mapper
public interface ReplyMapper {
	public void insertReply(Reply reply);
	public List<Reply> getReply(int postId);
	public List<Member> selectNickname(int replyerId);
	public void insertReReply(Reply reply);
	public List<ReReply> selectReReplyById(int postId); 
	public void deleteReplybypostCode (int postId);
	public int replyCnt(@Param("postId") int postId);
}
