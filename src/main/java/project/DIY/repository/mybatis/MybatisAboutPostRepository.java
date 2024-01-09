package project.DIY.repository.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.Likes;
import project.DIY.repository.AboutPostRepository;


@Repository
@RequiredArgsConstructor
@Primary
public class MybatisAboutPostRepository implements AboutPostRepository {
	@Autowired
	private final AboutPostMapper aboutPostMapper;

	@Override
	public void insertLikes(Likes likes) {
		aboutPostMapper.insertLikes(likes);
	}


	@Override
	public int selectLikes(Likes likes) {
		int selectLikescnt = aboutPostMapper.selectLikes(likes);
		return selectLikescnt;
	}


	@Override
	public void deleteLikes(Likes likes) {
		aboutPostMapper.deleteLikes(likes);
		
	}
	
	
	

}
