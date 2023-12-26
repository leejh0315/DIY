package project.DIY.repository.mybatis;

import project.DIY.domain.UploadFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UploadFileMapper {
	
	public UploadFile findById(String fileId);
	public int save(UploadFile uploadFile);
	public int selectBySaveFileName(String saveFileName);

}
