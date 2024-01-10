package project.DIY.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;

import project.DIY.domain.Likes;
import project.DIY.domain.ReportPost;

@Mapper
public interface AboutPostMapper {
	public void insertLikes(Likes likes);
	public int selectLikes(Likes likes);
	public void deleteLikes(Likes likes);
	
	public void insertReportPost(ReportPost reportpost);
}	
	