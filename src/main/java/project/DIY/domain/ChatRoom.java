package project.DIY.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.web.socket.WebSocketSession;

import lombok.Builder;
import lombok.Data;

@Data
public class ChatRoom {
	private int id;
    private String roomId;
    private String name;
    private int chatReceiverId;
    private int ChatSenderId;
    private Set<WebSocketSession> sessions = new HashSet<>();
    @Builder
    public ChatRoom(String roomId, String name, int chatReceiverId, int chatSenderId) {
        this.roomId = roomId;
        this.name = name;
        this.chatReceiverId = chatReceiverId;
        this.ChatSenderId = chatSenderId;
    }
}