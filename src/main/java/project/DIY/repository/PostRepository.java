package project.DIY.repository;

import java.util.List;

import project.DIY.domain.Post;

public interface PostRepository {
	public void insertPost(Post post);
	public Post selectPost();
	public String getLastPost();
	public Post selectByPostCode(int postCode);
	public List<Post> selectByType(String type);
<<<<<<< HEAD
	public List<Post> selectUserPostbyId(int meberId);
=======
	public List<Post> selectByPostCtCodeHome(String postCtCode);
>>>>>>> c8323e938397b24466f62554e3c5b290c7d36b97
}
