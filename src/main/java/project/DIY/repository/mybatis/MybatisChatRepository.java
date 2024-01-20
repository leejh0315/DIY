package project.DIY.repository.mybatis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.ChatMessage;
import project.DIY.domain.ChatRoom;
import project.DIY.repository.ChatRepository;

@Repository
@RequiredArgsConstructor
@Primary
public class MybatisChatRepository implements ChatRepository{
	
	@Autowired
	private final ChatMapper chatMapper;
	
	@Override
	public void insertChatRoom(ChatRoom chatRoom) {
		chatMapper.insertChatRoom(chatRoom);
	}

	@Override
	public List<ChatRoom> selectMyRoom(int id) {
		List<ChatRoom> rooms = chatMapper.selectMyRoom(id);
		return rooms;
	}

	@Override
	public ChatRoom findRoomByChatroomId(String chatroomId) {
		ChatRoom room = chatMapper.findRoomByChatroomId(chatroomId);
		return room;
	}

	@Override
	public List<ChatMessage> selectMessageByroomId(String randomId) {
		List<ChatMessage> messages = chatMapper.selectMessageByroomId(randomId);
		return messages;
	}

	@Override
	public void insertMessageByroomId(ChatMessage chatMessage) {
		chatMapper.insertMessageByroomId(chatMessage);
		
	}

}
