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
import project.DIY.domain.Notice;
import project.DIY.repository.AboutPostRepository;
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
    @Autowired
    private final AboutPostRepository aboutPostRepository;
    
    
    @GetMapping("/chat/chatList/{id}")
    public String chatList(Model model,
    		@PathVariable("id") int id,
    		HttpServletRequest req){
    	
        //List<ChatRoom> roomList = chatService.findAllRoom();
		HttpSession session = req.getSession(false);
		Member member = (Member) session.getAttribute(SessionVar.LOGIN_MEMBER);
		aboutNotice(member, model);
		
		
		
		//aboutPostRepository.updateNoticeView(1, id);
        List<ChatRoom> roomList = chatService.findMyRoom(id); //내 방들을 찾음( 내가 receiver이거나 sender이거나 )
        Map<Integer, Member> roomMap = new HashMap<Integer, Member>(); 
        
        List<ChatMessage> chatOrder = chatRepository.selectMessageByOrder(id);//(최근에 온 메세지가 상단에 노출되게끔 하는)
        
        System.out.println("roomList : " + roomList);
        
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
        System.out.println("this TempList : " + tempList);
       
        for(int i = 0; i<tempList.size(); i++) {//채팅방 개수만큼 반복
        	
        	if(member.getId() == tempList.get(i).getChatReceiverId()) { //세션에 있는 아이디와, tempList의 reciever가 같으면
        		
        			int you =tempList.get(i).getChatSenderId();			//상대방은 senderId 일것
        			if(you != member.getId()) {
        				Member uMember = memberRepository.selectBymemberId(you);
        				roomMap.put(you, uMember);
        				tempList.get(i).setChatReceiverId(you);
        		}
        	}
        	else if(member.getId() == tempList.get(i).getChatSenderId()) {
        		
        			int you =tempList.get(i).getChatReceiverId();
        			if(you != member.getId()) {
        				Member uMember = memberRepository.selectBymemberId(you);
        				roomMap.put(you, uMember);
        			}
        		
        	}
        }
        
        System.out.println("tempList : " + tempList);
        System.out.println("chatOrder : " + chatOrder);
        
        Map<Integer, Integer> chatCntMap = new HashMap<Integer, Integer>();
                
        for(int i = 0 ; i < chatOrder.size(); i++) {
        	Notice notice = new Notice();
        	notice.setTargetId(tempList.get(i).getId());
        	notice.setTargetMemberId(id);
        	
        	
        	
        	if(id == tempList.get(i).getChatSenderId()) {
        		notice.setDoMemberId(tempList.get(i).getChatReceiverId());
        	}else {
        		notice.setDoMemberId(tempList.get(i).getChatSenderId());
        	}
        	
        	 
        	System.out.println("thisNotice : " + notice);
            int cnt = chatRepository.chatRoomCount(notice);
            chatCntMap.put(tempList.get(i).getId(), cnt);
        }
        System.out.println("tempList :" + tempList);
        System.out.println("chatCntMap : " + chatCntMap);
        
        model.addAttribute("roomMap", roomMap);
        model.addAttribute("tempList", tempList);
        model.addAttribute("id", member.getId());
        model.addAttribute("member", member);
        model.addAttribute("chatOrder", chatOrder);
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
		
		if(name.equals("admin")) {
			receiverId = 13;
		}
		
    	String randomId = UUID.randomUUID().toString();
    	System.out.println("채팅 create 진입");
        chatService.createRoomDB(randomId, "", receiverId, senderId);
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
		aboutNotice(member, model);
        ChatRoom room = chatService.findByRoomId(roomId);
        
        
        if(member.getId() != room.getChatReceiverId() && member.getId() != room.getChatSenderId()) {
        	return "redirect:/chat/chatList/"+member.getId();
        }
        
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
    public void aboutNotice(Member member, Model model) {
	      if(member != null) {
	    	  List<Notice> noticeList = aboutPostRepository.selectNoticeById(member.getId());
	          int noticeCnt = 0;
	          int chatCnt = 0;
	          for(int i = 0 ; i < noticeList.size(); i++) {
	        	  if(!noticeList.get(i).getType().equals("chat")&& noticeList.get(i).getView()==0) noticeCnt++;
	        	  else if(noticeList.get(i).getType().equals("chat") && noticeList.get(i).getView()==0) chatCnt++;
	          }
	          model.addAttribute("chatCnt", chatCnt);
	          model.addAttribute("noticeCnt", noticeCnt);  
	      }
	   
 	}
    
}