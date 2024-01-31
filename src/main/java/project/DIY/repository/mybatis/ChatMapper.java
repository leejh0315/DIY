package project.DIY.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import project.DIY.domain.ChatMessage;
import project.DIY.domain.ChatRoom;

@Mapper
public interface ChatMapper {

	public List<ChatRoom> selectMyRoom(int id);
	public void insertChatRoom(ChatRoom chatRoom);
	public ChatRoom findRoomByChatroomId(String randomId);

	public List<ChatMessage> selectMessageByroomId(String randomId);
	public void insertMessageByroomId(ChatMessage chatMessage);
	public List<ChatMessage> selectMessageByOrder(int id);
}
