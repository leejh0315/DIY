package project.DIY.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AboutPostMapper {
	public void insertLikes(int memberId, int postCode);
}	
