package project.DIY.repository;

import java.util.List;

import io.lettuce.core.dynamic.annotation.Param;
import project.DIY.domain.Member;
import project.DIY.domain.ReReply;
import project.DIY.domain.Reply;

public interface ReplyRepository {
	public void insertReply(Reply reply);
	public List<Reply> getReply(int postId);
	public List<Member> selectNickname(int replyerId);
	public void deleteReplybypostCode (int postId);
	public void insertReReply(Reply reply);
	public List<ReReply> selectReReplyById(int postId);
	public int replyCnt(@Param("postId") int postId);
	public void deleteReply(int replyId);
	public void deleteReplyNotice(String type, int postCode, int doMemberId);
}
