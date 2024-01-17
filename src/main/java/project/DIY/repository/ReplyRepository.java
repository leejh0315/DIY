package project.DIY.repository;

import java.util.List;

import project.DIY.domain.Member;
import project.DIY.domain.Reply;

public interface ReplyRepository {
	public void insertReply(Reply reply);
	public List<Reply> getReply(int postId);
	public List<Member> selectNickname(int replyerId);
	public void deleteReplybypostCode (int postId);
	

}
