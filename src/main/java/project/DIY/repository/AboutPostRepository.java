package project.DIY.repository;

import project.DIY.domain.Likes;

public interface AboutPostRepository {
	public void insertLikes(Likes likes);
	public int selectLikes(Likes likes);
	public void deleteLikes(Likes likes);
}	
