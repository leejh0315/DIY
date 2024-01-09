package project.DIY.repository;

import java.util.List;

import project.DIY.domain.Post;

public interface PostRepository {
	public void insertPost(Post post);
	public Post selectPost();
	public Post getLastPost();
	public Post selectByPostCode(int postCode);
	public List<Post> selectByType(String type);
	public List<Post> selectUserPostbyId(int meberId);
}
