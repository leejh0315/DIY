package project.DIY.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import project.DIY.domain.Post;

@Mapper
public interface PostMapper {

	public void insertPost(Post post);
	public Post selectPost();
	public String getLastPost();
	public Post selectByPostCode(int postCode);
	public List<Post> selectByType(String type);
	public List<Post> selectByPostCtCodeHome(String postCtCode);
		
	
}
