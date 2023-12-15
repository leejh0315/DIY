package project.DIY.controller;

import java.time.LocalTime;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.Member;
import project.DIY.service.EmailService;

@RequiredArgsConstructor
@org.springframework.stereotype.Controller
public class EmailController {
	private final EmailService emailService;
	private String emailConfirm;
	
	@GetMapping("/emailConfirm")
	public String getemail() {
		return "confirm";
	}
	
	
	@ResponseBody
	@PostMapping("/emailConfirm")
	public String emailConfirm(@RequestParam(value = "email") String email) throws Exception {
		LocalTime now = LocalTime.now();
		System.out.println(now);
		String confirm = emailService.sendSimpleMessage(email);
		emailConfirm = confirm;

		return confirm;
	}
	
	@Cacheable(value = "Member", key = "#id", cacheManager = "rcm")
	@PostMapping("/numberCheck")
	public String numberCheck(@RequestParam(value = "number") String number) {
		if(number != emailConfirm) {
			System.out.println("번호 같음");
			return "1";
		}else {
			System.out.println("번호 다름");
			return "0";
		}
		
		
	}
} 	
