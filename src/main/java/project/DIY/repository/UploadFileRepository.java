package project.DIY.repository;

import project.DIY.domain.UploadFile;

public interface UploadFileRepository {

	public int save(UploadFile uploadFile);
	public UploadFile findById(String fileId);
	public int selectBySaveFileName(String saveFileName);
}
