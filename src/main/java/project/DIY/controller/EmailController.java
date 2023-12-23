package project.DIY.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import project.DIY.service.EmailService;
import project.DIY.service.RedisUtils;

@RequiredArgsConstructor
@org.springframework.stereotype.Controller
public class EmailController {
	
	
	private final EmailService emailService;
	private final RedisUtils redisUtils;
	private String userEmail;
	
	
	@GetMapping("/emailConfirm")
	public String getemail() {
		return "confirm";
	}
	
	
	@ResponseBody
	@PostMapping("/emailConfirm")
	public String emailConfirm(@RequestParam(value = "email") String email) throws Exception {
		userEmail = email;
		String confirm = emailService.sendSimpleMessage(email);
		return confirm;
	}
	
	@ResponseBody
	@PostMapping("/numberCheck")
	public String numberCheck(@RequestParam(value = "number") String number) {
		
		if(number.equals(redisUtils.getData(userEmail))) {
			redisUtils.setDataExpire(userEmail, "Y", 60*10L);
			System.out.println("번호 같음");
			return "1";
		}else if(redisUtils.getData(userEmail) == "" || redisUtils.getData(userEmail) == null) {
			System.out.println("레디스 비었음");
			return "2";
		}
		else {
			System.out.println("번호 다름");
			return "0";
		}
		
	}
} 	
