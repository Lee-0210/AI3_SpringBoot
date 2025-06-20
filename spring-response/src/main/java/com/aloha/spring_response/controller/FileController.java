package com.aloha.spring_response.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aloha.spring_response.util.MediaUtil;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/file")
public class FileController {

    /**
     * 이미지 썸네일
     * ✅ 요청 경로 : /file/img?filePath=C:/upload/sample.png
     * @param filePath
     * @param res
     * @throws IOException
     */
    @GetMapping("/img")
    public void showImg(@RequestParam("filePath") String filePath, HttpServletResponse res) throws Exception {
        log.info("filePath : {}", filePath);
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        ServletOutputStream sos = res.getOutputStream();
        FileCopyUtils.copy(fis, sos);

        // 이미지 썸네일로 응답이 되도록 헤더 세팅
        // - 확장자로 컨텐츠 타입 지정
        String ext = filePath.substring(filePath.lastIndexOf(".") + 1);
        MediaType mediaType = MediaUtil.getMediaType(ext);
        if(mediaType == null) return;
        log.info("mediaType {}", mediaType);
        res.setContentType(mediaType.toString());
    }

    @GetMapping("/download")
    public void fileDownload(@RequestParam("filePath") String filePath, HttpServletResponse res) throws Exception {

        log.info("filePath : {}", filePath);

        // 파일 다운로드를 위한 헤더 세팅
        // - ContentType            : application/octet-stream
        // - Content-Disposition    : attachment; filename="파일명.확장자"
        File file = new File(filePath);
        String fileName = file.getName();
        res.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        res.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        // 다운로드
        FileInputStream fis = new FileInputStream(file);
        ServletOutputStream sos = res.getOutputStream();
        FileCopyUtils.copy(fis, sos);
    }
}
