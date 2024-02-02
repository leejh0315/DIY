package project.DIY.domain;

import lombok.Data;

@Data
public class ChatMessage {
    // 메시지 타입 : 입장, 채팅, 나감
    public enum MessageType {
        ENTER, TALK,QUIT
    }
    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private int sender; // 메시지 보낸사람
    private int receiver;
    private String message; // 메시지
    private String sendDate;	//보낸 날짜
}