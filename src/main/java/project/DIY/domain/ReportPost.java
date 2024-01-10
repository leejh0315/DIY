package project.DIY.domain;

import java.util.Date;

import lombok.Data;

@Data
public class ReportPost {
	private int reportpostId; 
	private int postCode; 
	private String title; 
	private String content; 
	private int memberId; 
	private int reporterId; 
	private Date reportedDate;

}

