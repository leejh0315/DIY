package project.DIY.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;

import project.DIY.domain.Reply;

@Mapper
public interface ReplyMapper {
	public void insertReply(Reply reply);

}
