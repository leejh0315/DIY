package project.DIY.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import project.DIY.service.EmailService;
import project.DIY.service.RedisUtils;

@RequiredArgsConstructor
@org.springframework.stereotype.Controller
@RequestMapping("/email")
public class EmailController {
	
	
	private final EmailService emailService;
	private final RedisUtils redisUtils;
	private String userEmail;
	
	
	@GetMapping("/emailConfirm")
	public String getemail() {
		return "confirm";
	}
	
	//이메일 전송 및 난수 반환
	@ResponseBody
	@PostMapping("/emailConfirm")
	public String emailConfirm(@RequestParam(value = "email") String email) throws Exception {
		userEmail = email;
		String confirm = emailService.sendSimpleMessage(email);
		return confirm;
	}
	
	//이메일로 전송된 난수 Redis에서 체크
	@ResponseBody
	@PostMapping("/numberCheck")
	public String numberCheck(@RequestParam(value = "number") String number) {
		
		if(number.equals(redisUtils.getData(userEmail))) {
			redisUtils.setDataExpire(userEmail, "Y", 60*10L);
			return "1";
		}else if(redisUtils.getData(userEmail) == "" || redisUtils.getData(userEmail) == null) {
			return "2";
		}else {
			return "0";
		}
	}
} 	
