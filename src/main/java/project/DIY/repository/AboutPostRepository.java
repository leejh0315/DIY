package project.DIY.repository;

import project.DIY.domain.Likes;
import project.DIY.domain.ReportPost;

public interface AboutPostRepository {
	public void insertLikes(Likes likes);
	public int selectLikes(Likes likes);
	public void deleteLikes(Likes likes);
	
	public void insertReportPost(ReportPost reportpost);
}	
