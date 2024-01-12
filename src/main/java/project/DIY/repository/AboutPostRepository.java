package project.DIY.repository;

import java.util.List;

import project.DIY.domain.Likes;
import project.DIY.domain.ReportPost;

public interface AboutPostRepository {
	public void insertLikes(Likes likes);
	public int selectLikes(Likes likes);
	public void deleteLikes(Likes likes);
	
	public void insertReportPost(ReportPost reportpost);
	public int selectReportPost(ReportPost reportpost);
	
	public List<ReportPost> selectAllReportPost();
}	
