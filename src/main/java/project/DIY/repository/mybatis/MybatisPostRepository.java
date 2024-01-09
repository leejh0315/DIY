package project.DIY.repository.mybatis;

import java.util.List;

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

	@Override
	public Post getLastPost() {
		Post post = postMapper.getLastPost();
		return post;
	}

	@Override
	public Post selectByPostCode(int postCode) {
		Post post = postMapper.selectByPostCode(postCode);
		return post;
	}

	@Override
	public List<Post> selectByType(String type) {
		List<Post> post = postMapper.selectByType(type);
		return post;
	}

	@Override
	public List<Post> selectUserPostbyId(int meberId) {
		List<Post> post = postMapper.selectUserPostbyId(meberId);
		return post;
	}
	
	
	

}
