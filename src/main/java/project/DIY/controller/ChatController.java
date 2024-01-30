package project.DIY.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.DIY.domain.ChatMessage;
import project.DIY.domain.ChatRoom;
import project.DIY.domain.Member;
import project.DIY.repository.ChatRepository;
import project.DIY.repository.MemberRepository;
import project.DIY.service.ChatService;
import project.DIY.session.SessionVar;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @Autowired
    private final ChatRepository chatRepository;
    @Autowired
    private final MemberRepository memberRepository;
    
    
    
    @GetMapping("/chat/chatList/{id}")
    public String chatList(Model model,
    		@PathVariable("id") int id,
    		HttpServletRequest req){
    	
        //List<ChatRoom> roomList = chatService.findAllRoom();
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
    	
		
        List<ChatRoom> roomList = chatService.findMyRoom(id);
        Map<Integer, Member> roomMap = new HashMap<Integer, Member>();
        
        List<ChatMessage> chatOrder = chatRepository.selectMessageByOrder();
        
        System.out.println(roomList);
        
        List<ChatRoom> tempList = new ArrayList<ChatRoom>();
        for(int i = 0 ; i < chatOrder.size(); i++) {
        	String tempId = chatOrder.get(i).getRoomId();
        	ChatRoom tempRoom = chatRepository.findRoomByChatroomId(tempId);
        	System.out.println(tempId);
        	for(int j = 0 ; j<roomList.size(); j++) {
        		if(tempId.equals(roomList.get(j).getRoomId())) {
        			tempList.add(tempRoom);
        		}
        	}
        }
        
        for(int i = 0; i<tempList.size(); i++) {
        	if(member.getId() == tempList.get(i).getChatReceiverId()) {
        		for(int j = 0 ; j < tempList.size(); j++) {
        			int you =tempList.get(j).getChatSenderId();
        			if(you != member.getId()) {
        				Member uMember = memberRepository.selectBymemberId(you);
        				roomMap.put(you, uMember);
        				tempList.get(i).setChatReceiverId(you);
        			}
        		}
        	}
        	else if(member.getId() == tempList.get(i).getChatSenderId()) {
        		for(int j = 0 ; j < tempList.size(); j++) {
        			int you =tempList.get(j).getChatReceiverId();
        			if(you != member.getId()) {
        				Member uMember = memberRepository.selectBymemberId(you);
        				roomMap.put(you, uMember);
        			}
        		}
        	}
        }
        System.out.println(roomMap);
        /*
        for(int i = 0; i<roomList.size(); i++) {
        	if(member.getId() == roomList.get(i).getChatReceiverId()) {
        		for(int j = 0 ; j < roomList.size(); j++) {
        			int you =roomList.get(j).getChatSenderId();
        			if(you != member.getId()) {
        				Member uMember = memberRepository.selectBymemberId(you);
        				roomMap.put(you, uMember);
        				roomList.get(i).setChatReceiverId(you);
        			}
        		}
        	}
        	else if(member.getId() == roomList.get(i).getChatSenderId()) {
        		for(int j = 0 ; j < roomList.size(); j++) {
        			int you =roomList.get(j).getChatReceiverId();
        			if(you != member.getId()) {
        				Member uMember = memberRepository.selectBymemberId(you);
        				roomMap.put(you, uMember);
        			}
        		}
        	}
        }
        */
        model.addAttribute("roomMap", roomMap);
        model.addAttribute("roomList", roomList);
        model.addAttribute("tempList", tempList);
        model.addAttribute("id", member.getId());
        model.addAttribute("member", member);
        return "chat/chatList";
    }


    @GetMapping("/chat/createRoom/{id}")  //방을 만들었으면 해당 방으로 가야지.
    public String createRoom(Model model, HttpServletRequest req,
    		@RequestParam(value = "name", defaultValue = "") String name,
    		@PathVariable("id") int id
    		) {
    	
    	HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		
		int senderId = member.getId();
		int receiverId = id;
			
    	String randomId = UUID.randomUUID().toString();
    	
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
        model.addAttribute("id", member.getId());
        return "chat/chatRoom";
    }
}