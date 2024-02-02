package project.DIY.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.DIY.domain.ChatMessage;
import project.DIY.domain.ChatRoom;
import project.DIY.domain.Notice;
import project.DIY.repository.AboutPostRepository;
import project.DIY.repository.ChatRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSockChatHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    @Autowired
    private final ChatRepository chatRepository;
    @Autowired
    private final AboutPostRepository aboutPostRepository;
    
    private final Set<WebSocketSession> Setsessions = new CopyOnWriteArraySet<>();
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	Setsessions.add(session);
        System.out.println("afterConnectionEstablished : " + Setsessions );
    }

   @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        ChatRoom room = chatService.findByRoomId(chatMessage.getRoomId());
        String goalRoom = "ws://localhost:8080/ws/chat/" + chatMessage.getRoomId();
        
        room.setSessions(Setsessions);
        
        Set<WebSocketSession> sessions= room.getSessions();   //방에 있는 현재 사용자 한명이 WebsocketSession
        
        //if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
        if (chatMessage.getMessage().equals("") && chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            
            //사용자가 방에 입장하면  Enter메세지를 보내도록 해놓음.  이건 새로운사용자가 socket 연결한 것이랑은 다름.
            //socket연결은 이 메세지 보내기전에 이미 되어있는 상태
            sessions.add(session);
            //chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");  //TALK일 경우 msg가 있을 거고, ENTER일 경우 메세지 없으니까 message set
            //sendToEachSocket(sessions,new TextMessage(objectMapper.writeValueAsString(chatMessage)),goalRoom);
        }else if (chatMessage.getMessage().equals("") && chatMessage.getType().equals(ChatMessage.MessageType.QUIT)) {
            
        	sessions.remove(session);
            //chatMessage.setMessage(chatMessage.getSender() + "님이 퇴장했습니다..");
            //sendToEachSocket(sessions,new TextMessage(objectMapper.writeValueAsString(chatMessage)),goalRoom);
        }else {
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);
            
            
        	System.out.println("chatMessage :"+ chatMessage.getMessage());
        	
        	ChatMessage cm = new ChatMessage();
        	cm.setRoomId(chatMessage.getRoomId());
        	cm.setSender(chatMessage.getSender());
        	cm.setMessage(chatMessage.getMessage());
        	cm.setSendDate(formattedDateTime);
        	System.out.println(payload);
        	
        	
        	Notice notice = new Notice();
        	notice.setDoMemberId(chatMessage.getSender());
        	notice.setType("chat");
        	String rId = chatMessage.getRoomId();
        	ChatRoom nowR = chatRepository.findRoomByChatroomId(rId);
        	
        	if(nowR.getChatReceiverId() == chatMessage.getSender()) {
        		notice.setTargetMemberId(nowR.getChatSenderId());
        		cm.setReceiver(nowR.getChatSenderId());
        	}else {
        		notice.setTargetMemberId(nowR.getChatReceiverId());
        		cm.setReceiver(nowR.getChatReceiverId());
        	}
        	notice.setTargetId(nowR.getId());
        	notice.setNoticeOn(currentDateTime);
        	System.out.println("notice : " + notice);
        	aboutPostRepository.insertNotice(notice);
        	
        	if(!chatMessage.getMessage().equals("")) {
        		System.out.println("notNull");
        		System.out.println("cm : " + cm);
        		chatRepository.insertMessageByroomId(cm);
        	}
        	
            sendToEachSocket(sessions,message,goalRoom); //입장,퇴장 아닐 때는 클라이언트로부터 온 메세지 그대로 전달.
            
        }
    }
    private  void sendToEachSocket(Set<WebSocketSession> sessions, TextMessage message, String goalRoom){
        sessions.parallelStream().forEach( roomSession -> {
            try {
            	System.out.println("roomSession : " + roomSession);
            	System.out.println("roomSession : " + roomSession.getUri());
            	if(roomSession.getUri().toString().equals(goalRoom)) {
            		roomSession.sendMessage(message);
            	}
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }



    @Override 	
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	if(Setsessions.contains(session)) {
    		Setsessions.remove(session);
    	}else {
    		System.out.println("안겹침");
    	}
    	
       //javascript에서  session.close해서 연결 끊음. 그리고 이 메소드 실행.
        //session은 연결 끊긴 session을 매개변수로 이거갖고 뭐 하세요.... 하고 제공해주는 것 뿐
    	
    }



}