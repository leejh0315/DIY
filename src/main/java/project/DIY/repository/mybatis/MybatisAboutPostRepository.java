package project.DIY.repository.mybatis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.Likes;
import project.DIY.domain.Notice;
import project.DIY.domain.ReportPost;
import project.DIY.repository.AboutPostRepository;


@Repository
@RequiredArgsConstructor
@Primary
public class MybatisAboutPostRepository implements AboutPostRepository {
	@Autowired
	private final AboutPostMapper aboutPostMapper;

	@Override
	public void insertLikes(Likes likes) {
		aboutPostMapper.insertLikes(likes);
	}


	@Override
	public int selectLikes(Likes likes) {
		int selectLikescnt = aboutPostMapper.selectLikes(likes);
		return selectLikescnt;
	}


	@Override
	public void deleteLikes(Likes likes) {
		aboutPostMapper.deleteLikes(likes);
		
	}


	@Override
	public void insertReportPost(ReportPost reportpost) {
		aboutPostMapper.insertReportPost(reportpost);
		
	}


	@Override
	public int selectReportPost(ReportPost reportpost) {
		int selectReportPostcnt = aboutPostMapper.selectReportPost(reportpost);
		return selectReportPostcnt;
	}


	@Override
	public List<ReportPost> selectAllReportPost() {
		List<ReportPost> allReportPost = aboutPostMapper.selectAllReportPost();
		return allReportPost;
	}


	@Override
	public void deleteReportPost(int postCode) {
		aboutPostMapper.deleteReportPost(postCode);
	}


	@Override
	public void deletePostLikes(int postCode) {
		aboutPostMapper.deletePostLikes(postCode);
		
	}


	@Override
	public void insertNotice(Notice notice) {
		aboutPostMapper.insertNotice(notice);
		
	}


	@Override
	public List<Notice> selectNoticeById(int memberId) {
		List<Notice> noticeList = aboutPostMapper.selectNoticeById(memberId);
		return noticeList;
	}
	
	
	

}
