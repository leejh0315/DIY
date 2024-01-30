package project.DIY.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import project.DIY.domain.Likes;
import project.DIY.domain.ReportPost;

@Mapper
public interface AboutPostMapper {
	public void insertLikes(Likes likes); //좋아요 추가
	public int selectLikes(Likes likes); //좋아요 유무 확인(result:0 or 1)
	public void deleteLikes(Likes likes); //좋아요 삭제
	public List<Integer> selectLikedPostsById (int memberId);
	public void deletePostLikes(int postCode); //PostCode에 해당하는 게시글 좋아요 삭제
	public void insertReportPost(ReportPost reportpost); // 게시글 신고
	public int selectReportPost(ReportPost reportpost); // 게시글 신고 확인
	public List<ReportPost> selectAllReportPost(); //신고게시글 전체
	public void deleteReportPost(int postCode); //신고게시글 삭제
}	
	

