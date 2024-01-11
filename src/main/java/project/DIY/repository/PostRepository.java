package project.DIY.repository;

import java.util.List;

import project.DIY.domain.PaginationVo;
import project.DIY.domain.Post;

public interface PostRepository {
	public void insertPost(Post post);
	public String getLastPost();
	public Post selectByPostCode(int postCode);
	public List<Post> selectByType(String type);
	public List<Post> selectUserPostbyId(int meberId);
	public List<Post> selectByPostCtCodeHome(String postCtCode);
	public void updatePostByPostCode(Post post);
	public List<Post> getPostsByPageByMemberId(PaginationVo paginationVo);
}
