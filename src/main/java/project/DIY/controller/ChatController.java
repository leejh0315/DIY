package project.DIY.controller;

import java.util.List;

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
import project.DIY.domain.ChatRoom;
import project.DIY.domain.Member;
import project.DIY.service.ChatService;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor

public class ChatController {
    private final ChatService chatService;


    @RequestMapping("/chat/chatList/{id}")
    public String chatList(Model model,
    		@PathVariable("id") String id
    		){
    	/*
        List<ChatRoom> roomList = chatService.findMyRoom(Integer.parseInt(id));
        model.addAttribute("roomList",roomList);
        return "chat/chatList";
        */
        
        List<ChatRoom> roomList = chatService.findAllRoom();
        model.addAttribute("roomList",roomList);
        return "chat/chatList";
        
    }

/*
 * @RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "type", defaultValue = "all") String type
 * 
 * */
    @PostMapping("/chat/createRoom")  //방을 만들었으면 해당 방으로 가야지.
    public String createRoom(Model model,
    		@RequestParam(value = "name", defaultValue = "") String name,
    		@RequestParam(value = "username", defaultValue = "") String username
    		) {
    	
        ChatRoom room = chatService.createRoom(name);
        model.addAttribute("room",room);
        model.addAttribute("username",username);
        return "chat/chatRoom";  //만든사람이 채팅방 1빠로 들어가게 됩니다
    }

    @GetMapping("/chat/chatRoom")
    public String chatRoom(Model model,
    		@RequestParam(value = "roomId", defaultValue = "") String roomId
    		
    		){
        ChatRoom room = chatService.findRoomById(roomId);
        model.addAttribute("room",room);   //현재 방에 들어오기위해서 필요한데...... 접속자 수 등등은 실시간으로 보여줘야 돼서 여기서는 못함
        return "chat/chatRoom";
    }
}