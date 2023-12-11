package project.DIY.service;

public interface EmailService {
    String sendSimpleMessage(String to)throws Exception;

	String sendSimpleMessagePassword(String to, String password) throws Exception;
    
}