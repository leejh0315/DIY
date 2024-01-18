package project.DIY.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import project.DIY.domain.PaginationVo;
import project.DIY.domain.Post;

@Mapper
public interface PostMapper {

	public void insertPost(Post post); //게시글 등록
	public String getLastPost(int memberId); //마지막 작성된 post의 code를 반환
	public List<Post> selectAllpost(); //모든 게시글 반환
	public Post selectByPostCode(int postCode); //postCode로 해당 post 조회
	public List<Post> selectByType(String type); //카테고리에 맞는 post list를 반환
	public List<Post> selectUserPostbyId(int meberId); //해당 유저가 작성한 post list 반환
	public List<Post> selectByPostCtCodeHome(String postCtCode); //카테고리에 맞는 가장 많이 작성된 5개의 post를 반환
	public void updatePostByPostCode(Post post); //postCode에 맞는 post를 수정
	public List<Post> getPostsByPageByMemberId(PaginationVo paginationVo); //memberId에 해당하는 member의 게시글 반환
	public int getPostsCountByMemberId(PaginationVo paginationVo); //memberId에 해당하는 member의 게시글의 개수 반환
	public void deletePost(int postCode);//postCode에 해당하는 게시글 삭제
	
	public int selecetPosCntBySearch(String search); //검색어에 맞는 게시글 개수 반환
	public List<Post>selecetPostBySearch(PaginationVo paginationVo); //검색어에 맞는 게시글 반환
	public int countByMonth(int memberId, int year, int month);//memberId에 해당하는 멤버의 월별 게시글 개수 반환
	

	public void updateById(String memberNick, int postCode); //postCode에 해당하는 post의 작성자 이름을 업데이트
}
