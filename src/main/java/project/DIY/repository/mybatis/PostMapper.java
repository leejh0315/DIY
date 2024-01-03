package project.DIY.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;

import project.DIY.domain.Post;

@Mapper
public interface PostMapper {

	public void insertPost(Post post);
	public Post selectPost();
	public Post getLastPost();
	public Post selectByPostCode(int postCode);
		
	
}
