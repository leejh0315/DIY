package project.DIY.repository.mybatis;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.Post;
import project.DIY.repository.PostRepository;

@Repository
@RequiredArgsConstructor
@Primary
public class MybatisPostRepository implements PostRepository{

	private final PostMapper postMapper;

	@Override
	public void insertPost(Post post) {
		postMapper.insertPost(post);
		
	}

	@Override
	public Post selectPost() {
		Post post = postMapper.selectPost();
		return post;
	}
	

}
