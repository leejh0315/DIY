package project.DIY.repository;

import java.util.List;

import project.DIY.domain.PaginationVo;
import project.DIY.domain.Post;

public interface PostRepository {
	public void insertPost(Post post);
	public String getLastPost(int memberId);
	public List<Post> selectAllpost();
	public Post selectByPostCode(int postCode);
	public int selectByTypeCnt(String type);
	public List<Post> selectByType(PaginationVo paginationVo);
	public List<Post> selectUserPostbyId(int meberId);
	public List<Post> selectByPostCtCodeHome(String postCtCode);
	public void updatePostByPostCode(Post post);
	public void deletePost(int postCode);
	
	public List<Post> getPostsByPageByMemberId(PaginationVo paginationVo);
	public int getPostsCountByMemberId(PaginationVo paginationVo);
	
	public int selecetPosCntBySearch(String search);
	public List<Post>selecetPostBySearch(PaginationVo paginationVo);
	public int countByMonth(int memberId, int year, int month);
	
	public void updateById(String memberNick, int postCode);
}
