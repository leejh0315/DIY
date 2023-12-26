package project.DIY.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class ImageTempController {
    private static String realPath = "실제 파일 저장 경로";

    @Autowired
    private BlogService blogService;

    // 게시글 업로드 요청 로직
    @RequestMapping("reg")
    public String reg(HttpServletRequest req, HttpServletResponse resp, String content, Model model) {
        HttpSession session = req.getSession();
        if (session.getAttribute("true") != null) {
            // 기존 temp폴더에 저장된 이미지 표시를 위해 에디터에는 /temp로 경로가 지정되어 있다 
            // 이를 마지막 게시글 다음 번호로 설정한다.
            content = content.replaceAll("/temp", "/" + board_num);

            // temp 폴더 안의 이미지를 게시글 저장소로 이동
            String path_folder1 = realPath + "/upload_image/image/fileupload/temp /";
            String path_folder2 = realPath + "/upload_image/image/fileupload/" + board_num + "/";

            // 폴더 복사 함수
            fileUpload(path_folder1, path_folder2);

            // ...
        }
    }

    // 게시글 수정 페이지 요청
    @RequestMapping("update")
    public String update(HttpServletRequest req, Model model, String num) {
        HttpSession session = req.getSession();

        // ...

        // 더미 이미지 방지를 위해 기존 temp 폴더를 비워준다.
        String path = realPath + "/upload_image/image/fileupload/temp /";

        // 폴더 내부 파일 삭제 함수
        deleteFolder(path);

        // 게시글 저장소에 있는 파일들을 temp 파일로 업로드 
        // 게시글 수정중 변심으로 페이지 벗어놔도 원본 게시글의 이미지는 보존된다.
        String path_folder1 = realPath + "/upload_image/image/fileupload/" + board.getNum() + "/";
        String path_folder2 = realPath + "/upload_image/image/fileupload/temp /";

        // temp로 임시저장
        fileUpload(path_folder1, path_folder2);

        // 본글에있던 이미지 경로를 temp로 바꿔준다
        board.setContents(board.getContents().replaceAll("/" + board.getNum() + "/", "/temp /"));

        // ...
    }

    @RequestMapping("del_update")
    public String del_update(HttpServletRequest req, String num, String doit, String title, String content, String description,
            String category, MultipartFile thumbnail) {

        // ...

        // 본문내용에 temp 폴더로 바꾼 이미지 경로를 원래대로 설정 
        content = content.replaceAll("/temp", "/" + Integer.toString(_num));

        // 본문에 안들어간 파일들 삭제(temp 폴더)
        String filePath = realPath + "/upload_image/image/fileupload/temp /";

        // 더미 파일 삭제함수 매개변수 : 파일 목록, 파일 경로, 검사할 본문
        removeDummyFiles(getFileNamesFromFolder(filePath), filePath, content);

        // 본글의 폴더 비우기
        filePath = realPath + "/upload_image/image/fileupload/" + num + "/";
        for (String fileName : getFileNamesFromFolder(filePath)) {
            deleteFile(filePath, fileName);
        }

        // temp 에서 저장된 데이터들 업로드
        String path_folder1 = realPath + "/upload_image/image/fileupload/temp /";
        String path_folder2 = realPath + "/upload_image/image/fileupload/" + Integer.toString(_num) + "/";

        fileUpload(path_folder1, path_folder2);

        // ...
    }

    // 서머노트 이미지 업로드 temp 저장
    @RequestMapping(value = "/uploadSummernoteImageFile", produces = "application/json; charset=utf8")
    @ResponseBody
    public String uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile,
            HttpServletRequest request) {
        // JSON 객체 생성
        JsonObject jsonObject = new JsonObject();

        // 이미지 파일이 저장될 경로 설정
        String contextRoot = realPath + "/upload_image/image/fileupload/29/";
        String fileRoot = contextRoot;

        // 업로드된 파일의 원본 파일명과 확장자 추출
        String originalFileName = multipartFile.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // 새로운 파일명 생성 (고유한 식별자 + 확장자)
        String savedFileName = UUID.randomUUID() + extension;

        // 저장될 파일의 경로와 파일명을 나타내는 File 객체 생성
        File targetFile = new File(fileRoot + savedFileName);

        try {
            // 업로드된 파일의 InputStream 얻기
            java.io.InputStream fileStream = multipartFile.getInputStream();

            // 업로드된 파일을 지정된 경로에 저장
            FileUtils.copyInputStreamToFile(fileStream, targetFile);

            // JSON 객체에 이미지 URL과 응답 코드 추가
            jsonObject.addProperty("url", "/upload_image/image/fileupload/29/" + savedFileName);
            jsonObject.addProperty("responseCode", "success");
        } catch (IOException e) {
            // 파일 저장 중 오류가 발생한 경우 해당 파일 삭제 및 에러 응답 코드 추가
            FileUtils.deleteQuietly(targetFile);
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }

        // JSON 객체를 문자열로 변환하여 반환
        String a = jsonObject.toString();
        return a;
    }

    // 서머노트 이미지 삭제 temp에서
    @RequestMapping(value = "/deleteSummernoteImageFile", produces = "application/json; charset=utf8")
    @ResponseBody
    public void deleteSummernoteImageFile(@RequestParam("file") String fileName) {

        // 폴더 위치
        String filePath = realPath + "/upload_image/image/fileupload/temp /";

        // 해당 파일 삭제
        deleteFile(filePath, fileName);
    }

    // 하위 폴더 복사
    private void fileUpload(String path_folder1, String path_folder2) {
        // path_folder1에서 path_folder2로 파일을 복사하는 함수입니다.

        File folder1;
        File folder2;
        folder1 = new File(path_folder1);
        folder2 = new File(path_folder2);

        // 복사할 폴더들이 존재하지 않으면 생성합니다.
        if (!folder1.exists())
            folder1.mkdirs();
        if (!folder2.exists())
            folder2.mkdirs();

        // 폴더1에서 파일 목록을 가져옵니다.
        File[] target_file = folder1.listFiles();
        for (File file : target_file) {
            // 복사 대상 파일의 경로와 이름을 설정합니다.
            File temp = new File(folder2.getAbsolutePath() + File.separator + file.getName());

            if (file.isDirectory()) {
                // 대상이 폴더인 경우, 해당 폴더를 생성합니다.
                temp.mkdir();
            } else {
                FileInputStream fis = null;
                FileOutputStream fos = null;
                try {
                    // 파일 복사를 위해 FileInputStream과 FileOutputStream을 생성합니다.
                    fis = new FileInputStream(file);
                    fos = new FileOutputStream(temp);

                    byte[] b = new byte[4096];
                    int cnt = 0;
                    while ((cnt = fis.read(b)) != -1) {
                        // 버퍼를 사용하여 파일 내용을 읽고 복사합니다.
                        fos.write(b, 0, cnt);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // FileInputStream과 FileOutputStream을 닫습니다.
                    try {
                        fis.close();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // 하위 폴더 삭제
    private void deleteFolder(String path) {
        // 주어진 경로에 있는 폴더와 파일을 재귀적으로 삭제하는 함수입니다.

        File folder = new File(path);
        try {
            if (folder.exists()) {
                File[] folder_list = folder.listFiles();
                for (int i = 0; i < folder_list.length; i++) {
                    if (folder_list[i].isFile())
                        // 파일인 경우, 파일을 삭제합니다.
                        folder_list[i].delete();
                    else
                        // 폴더인 경우, 재귀적으로 폴더 내부의 파일 및 폴더를 삭제합니다.
                        deleteFolder(folder_list[i].getPath());
                    // 파일이나 폴더를 삭제합니다.
                    folder_list[i].delete();
                }
                // 폴더를 삭제합니다.
                folder.delete();
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    // 위치값으로 내부 파일 이름 가져오기
    private List<String> getFileNamesFromFolder(String folderName) {
        // 파일 이름을 저장할 리스트 생성
        List<String> fileNames = new ArrayList<>();

        // 주어진 폴더 경로를 기반으로 폴더 객체 생성
        File folder = new File(folderName);
        // 폴더 내의 파일들을 가져옴
        File[] files = folder.listFiles();
        if (files != null) {
            // 파일인 경우 파일 이름을 리스트에 추가
            for (File file : files) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }
        }
        // 파일 이름을 담은 리스트 반환
        return fileNames;
    }

    // 더미 파일 삭제
    private void removeDummyFiles(List<String> fileNames, String filePath, String contents) {
        // 주어진 파일 이름 리스트를 기반으로 파일을 삭제
        for (String fileName : fileNames) {
            // contents 문자열에 파일 이름이 포함되어 있지 않은 경우 파일 삭제
            if (!contents.contains(fileName)) {
                deleteFile(filePath, fileName);
            }
        }
    }

    // 파일 하나 삭제
    private void deleteFile(String filePath, String fileName) {
        // 주어진 파일 경로와 이름을 기반으로 파일 경로 객체 생성
        Path path = Paths.get(filePath, fileName);
        try {
            // 파일 삭제
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
