package project.DIY.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.DIY.domain.ChatRoom;
import project.DIY.repository.ChatRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
	
	@Autowired
	private final ChatRepository chatRepository;
	
    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }
    
    public List<ChatRoom> findMyRoom(int id){
    	return chatRepository.selectMyRoom(id);
    }

    public ChatRoom findRoomById(String roomId) {
    	System.out.println(chatRooms.get(roomId));
        return chatRooms.get(roomId);
    }
    public ChatRoom findByRoomId(String roomId) {
    	ChatRoom room = chatRepository.findRoomByChatroomId(roomId);
    	return room; 
    }
    

    public ChatRoom createRoom(String name) {
    	System.out.println("chatService name : " + name);
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .chatReceiverId(0)
                .chatSenderId(0)
                .build();
        chatRooms.put(randomId, chatRoom);
        System.out.println(chatRooms);
        return chatRoom;
    }
    public ChatRoom createRoomDB(String randomId, String name, int chatReceiverId, int chatSenderId) {
    	System.out.println("createRoomDB 접근");
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .chatReceiverId(chatReceiverId)
                .chatSenderId(chatSenderId)
                .build();
        chatRepository.insertChatRoom(chatRoom);
        ChatRoom newRoom = chatRepository.findRoomByChatroomId(randomId);
        chatRooms.put(randomId, newRoom);
        return newRoom;
    }
    
}