package project.DIY.repository.mybatis;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.ChatRoom;
import project.DIY.repository.ChatRepository;

@Repository
@RequiredArgsConstructor
@Primary
public class MybatisChatRepository implements ChatRepository{
	
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

}
