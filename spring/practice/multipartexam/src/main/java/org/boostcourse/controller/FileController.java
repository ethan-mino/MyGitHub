package org.boostcourse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class FileController {
    @GetMapping("/uploadform")
    public String uploadform(){
        return "uploadform";
    }

    @GetMapping("download")
    public void download(HttpServletResponse response){
        String filename ="connect.png";
        String saveFileName = "C:/tmp/connect.png";
        String contentType = "image/png";
        int fileLength = 116303;

        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Type", contentType);
        response.setHeader("Content-Length", "" + fileLength);
        response.setHeader("Pragma", "no-cache;");
        response.setHeader("Expires", "-1;");

        try(FileInputStream fis = new FileInputStream(saveFileName);    // 선언된 객체들에 대해서 try가 종료될 때 자동으로 자원을 해제해주는 기능 (fis.close(), out.close()하지 않아도 된다.)
            OutputStream out = response.getOutputStream();){
            int readCount = 0;
            byte[] buffer = new byte[1024];
            while((readCount = fis.read(buffer)) != -1){
                out.write(buffer, 0, readCount);
            }
        }catch(Exception e){
            throw new RuntimeException("file download Error");
        }
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        System.out.println("파일 이름 : " + multipartFile.getOriginalFilename());
        System.out.println("파일 크기 : " + multipartFile.getSize());

        String savePath = "c:/tmp/" + multipartFile.getOriginalFilename();

        System.out.println("code change");
        File convFile = new File(savePath);
        multipartFile.transferTo(convFile);
/*
        try(FileOutputStream fos = new FileOutputStream("c:/tmp/" + multipartFile.getOriginalFilename());
            InputStream is = multipartFile.getInputStream();){
            int readCount = 0;
            byte[] buffer = new byte[1024];
            while((readCount = is.read(buffer)) != -1){
                fos.write(buffer, 0, readCount);
            }
        }catch (Exception e){
            throw new RuntimeException("file save Error");
        }
*/
        return "uploadok";
    }


}