package project.DIY.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Image {
    private int ImageCode;		//이미지코드
    private String imageName;	//이미지 이름
    private String imageFiles;	//
    private String IType;		
    private String ITarget;
}