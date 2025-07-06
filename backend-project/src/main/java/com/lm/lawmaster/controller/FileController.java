package com.lm.lawmaster.controller;

import com.lm.lawmaster.entity.Posts;
import com.lm.lawmaster.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("files")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FileController {

    @Autowired
    private PostsService postsService;

    @GetMapping("/download/{imageName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String imageName) {
        try {
            // 加载资源文件，使用 ClassPathResource
            Resource resource = new ClassPathResource("static/files/" + imageName); // 假设文件位于 resources/static 目录下

            // 确保文件存在
            if (resource.exists()) {
                // 获取文件类型，如果无法获取，默认使用 application/octet-stream
                String contentType = Files.probeContentType(Paths.get(resource.getURI()));
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // 上传文件
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("filename") String newFileName, // 前端传来的新文件名
            @RequestParam("file") MultipartFile file  // 上传的文件
    ) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("文件不能为空");
            }

            // 获取文件后缀
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || !originalFileName.contains(".")) {
                return ResponseEntity.badRequest().body("无效的文件");
            }
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

            // 拼接最终文件名
            String finalFileName = newFileName + fileExtension;
            Path path = Paths.get("src/main/resources/static/files/"+ finalFileName );

            // 保存文件
            boolean a=Files.exists(path);
            Files.write(path, file.getBytes());

            // 返回文件路径（根据实际需求调整）
            String fileUrl = "http://localhost:8080/files/download/" + finalFileName;

            return ResponseEntity.ok().body("{\"success\": true, \"url\": \"" + fileUrl + "\"}");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"success\": false, \"message\": \"上传失败\"}");
        }
    }
}
