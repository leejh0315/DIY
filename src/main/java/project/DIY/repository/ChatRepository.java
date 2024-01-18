package project.DIY.repository;

import java.util.List;

import project.DIY.domain.ChatRoom;

public interface ChatRepository {
	public void insertChatRoom(ChatRoom chatRoom);
	public List<ChatRoom> selectMyRoom(int id);
	public ChatRoom findRoomByChatroomId(String chatroomId);
}
