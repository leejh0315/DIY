package project.DIY.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import project.DIY.service.EmailService;

@RequiredArgsConstructor
@org.springframework.stereotype.Controller
public class EmailController {
	private final EmailService emailService;
	
	@GetMapping("/emailConfirm")
	public String getemail() {
		return "confirm";
	}
	@PostMapping("/emailConfirm")
	@ResponseBody
	public String emailConfirm(@RequestParam(value = "email") String email) throws Exception {
		System.out.println("여기 진입");
		String confirm = emailService.sendSimpleMessage(email);
		return confirm;
	}
} 	
