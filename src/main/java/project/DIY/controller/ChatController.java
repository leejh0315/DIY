package project.DIY.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.ChatMessage;
import project.DIY.domain.ChatRoom;
import project.DIY.domain.Member;
import project.DIY.repository.ChatRepository;
import project.DIY.service.ChatService;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @Autowired
    private final ChatRepository chatRepository;
    
    
    
    @GetMapping("/chat/chatList/{id}")
    public String chatList(Model model,
    		@PathVariable("id") int id,
    		HttpServletRequest req){
    	
        //List<ChatRoom> roomList = chatService.findAllRoom();
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
    	
		
        List<ChatRoom> roomList = chatService.findMyRoom(id);
        model.addAttribute("roomList",roomList);
        return "chat/chatList";
    }


    @PostMapping("/chat/createRoom")  //방을 만들었으면 해당 방으로 가야지.
    public String createRoom(Model model, 
    		@RequestParam(value = "name", defaultValue = "") String name
    		
    		) {
    	String randomId = UUID.randomUUID().toString();
    	int receiverId= 11;
    	int senderId = 12;
    	
        chatService.createRoomDB(randomId, name, receiverId, senderId);
        ChatRoom newRoom = chatService.findByRoomId(randomId);
        
        System.out.println(newRoom);
        
        model.addAttribute("room",newRoom);
        return "redirect:/chat/chatRoom/"+randomId;  //만든사람이 채팅방 1빠로 들어가게 됩니다
    }

    @GetMapping("/chat/chatRoom/{roomId}")
    public String chatRoom(Model model,HttpServletRequest req,
    		@PathVariable("roomId") String roomId)
    		{
    	
    	HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		String memberNick = member.getNickName();
		
        ChatRoom room = chatService.findByRoomId(roomId);
        List<ChatMessage> originMessage = chatRepository.selectMessageByroomId(roomId);
    	//ChatRoom room = chatService.findRoomById(roomId);

        System.out.println(originMessage);
        model.addAttribute("message", originMessage);
        model.addAttribute("member", member);
        model.addAttribute("memberNick", memberNick);
        model.addAttribute("room",room);   //현재 방에 들어오기위해서 필요한데...... 접속자 수 등등은 실시간으로 보여줘야 돼서 여기서는 못함
        return "chat/chatRoom";
    }
}