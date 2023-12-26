package project.DIY.repository.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import project.DIY.domain.UploadFile;
import project.DIY.repository.UploadFileRepository;

@Primary
@Repository
@RequiredArgsConstructor
public class MybatisUploadFileRepository implements UploadFileRepository {
	
	@Autowired
	private final UploadFileMapper uploadFileMapper;
	
	@Override
	public UploadFile findById(String fileId) {
		UploadFile uploadFile = uploadFileMapper.findById(fileId);
		return uploadFile;
	}
	@Override
	public int save(UploadFile uploadFile) {
		System.out.println("mybatisuploadfileRepository uploadFile.getSaveFileName() : " + uploadFile.getSaveFileName());
		uploadFileMapper.save(uploadFile);
		int fileId = uploadFileMapper.selectBySaveFileName(uploadFile.getSaveFileName());
		uploadFile.setFileId(fileId);
		return uploadFile.getFileId();
	}
	
	@Override
	public int selectBySaveFileName(String saveFileName) {
	
		return 0;
	}

}
