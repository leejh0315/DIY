package project.DIY.repository;

import java.util.List;

import project.DIY.domain.ChatMessage;
import project.DIY.domain.ChatRoom;
import project.DIY.domain.Notice;

public interface ChatRepository {

	public List<ChatRoom> selectMyRoom(int id);
	public void insertChatRoom(ChatRoom chatRoom);
	public ChatRoom findRoomByChatroomId(String randomId);
	public List<ChatMessage> selectMessageByroomId(String randomId);
	public void insertMessageByroomId(ChatMessage chatMessage);
	public List<ChatMessage> selectMessageByOrder(int id);
	public int chatRoomCount(Notice notice);
}
