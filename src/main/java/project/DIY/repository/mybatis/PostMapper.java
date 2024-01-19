package project.DIY.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import project.DIY.domain.PaginationVo;
import project.DIY.domain.Post;

@Mapper
public interface PostMapper {

   public void insertPost(Post post);
   public String getLastPost(int memberId);
   public List<Post> selectAllpost();
   public Post selectByPostCode(int postCode);
   
   public int selectByTypeCnt(String type);
   public List<Post> selectByType(PaginationVo paginationVo);
   public List<Post> selectUserPostbyId(int meberId);
   public List<Post> selectByPostCtCodeHome(String postCtCode);
   public void updatePostByPostCode(Post post);
   public List<Post> getPostsByPageByMemberId(PaginationVo paginationVo);
   public int getPostsCountByMemberId(PaginationVo paginationVo);
   public void deletePost(int postCode);
   
   public int selecetPosCntBySearch(String search);
   public List<Post>selecetPostBySearch(PaginationVo paginationVo);
   public int countByMonth(int memberId, int year, int month);
   

   public void updateById(String memberNick, int postCode);
}