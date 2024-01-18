package project.DIY.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import project.DIY.domain.ChatRoom;

@Mapper
public interface ChatMapper {
	public void insertChatRoom(ChatRoom chatRoom);
	public List<ChatRoom> selectMyRoom(int id);
	public ChatRoom findRoomByChatroomId(String chatroomId);
}
