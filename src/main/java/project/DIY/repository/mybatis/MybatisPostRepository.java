package project.DIY.repository.mybatis;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.PaginationVo;
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
	public String getLastPost() {
		String postCode = postMapper.getLastPost();
		return postCode;
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
	
	

	public List<Post> selectByPostCtCodeHome(String postCtCode) {
		List<Post> post = postMapper.selectByPostCtCodeHome(postCtCode);
		return post;
	}

	@Override
	public void updatePostByPostCode(Post post) {
		postMapper.updatePostByPostCode(post);
		
	}

	@Override
	public List<Post> getPostsByPageByMemberId(PaginationVo paginationVo) {
		List<Post> post = postMapper.getPostsByPageByMemberId(paginationVo);
		return post;
	}

	@Override
	public int getPostsCountByMemberId(PaginationVo paginationVo) {
		int cnt = postMapper.getPostsCountByMemberId(paginationVo);
		return cnt;
	}

	@Override
	public int selecetPosCnttBySearch(String search) {
		int cnt = postMapper.selecetPosCnttBySearch(search);
		return cnt;
	}
	
	

	

}
