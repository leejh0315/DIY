package project.DIY.repository;

import project.DIY.domain.Post;

public interface PostRepository {
	public void insertPost(Post post);
	public Post selectPost();
}
