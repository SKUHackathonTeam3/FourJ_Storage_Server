package com.example.fileupload.controller;

import com.example.fileupload.domain.FileInfo;
import com.example.fileupload.dto.FileDto;
import com.example.fileupload.repository.FileInfoRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class FileUploadController {

    private final FileInfoRepository fileInfoRepository;

    @Value("${file.base-dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String showUploadForm(@RequestParam(value = "path", required = false) String path, Model model) {

        Path directoryPath = Paths.get(fileDir, (path == null ? "" : path));
        File[] files = directoryPath.toFile().listFiles();
        List<FileDto> fileList = new ArrayList<>();

        if (files != null) {

            for (File file : files) {

                String originalName = file.getName();
                LocalDateTime uploadedAt = null;
                System.out.println(originalName);
                if(!file.isDirectory()) {

                    Optional<FileInfo> fileInfoOptional = fileInfoRepository.findByStoredUrlContaining(originalName);
                    if (fileInfoOptional.isEmpty()) continue;
                    FileInfo fileInfo = fileInfoOptional.get();

                    originalName = fileInfo.getOriginalName();
                    uploadedAt = fileInfo.getUploadedAt();
                }

                String relativePath = file.getAbsolutePath().substring(fileDir.length() + 1);
                fileList.add(FileDto.builder()
                        .originalName(originalName)
                        .name(file.getName())
                        .isDirectory(file.isDirectory())
                        .path(relativePath.replace("\\", "/"))
                        .uploadedAt(uploadedAt)
                        .build());
            }
        }

        String parentPath = directoryPath.getParent() != null ? directoryPath.getParent().toString() : "";

        parentPath = parentPath.replace("\\", "/");

        if (parentPath.startsWith(fileDir)) {
            if (parentPath.equals(fileDir)) parentPath = "";
            else parentPath = parentPath.substring(fileDir.length() + 1);
        }

        model.addAttribute("baseDir", fileDir);
        model.addAttribute("files", fileList);
        model.addAttribute("currentPath", directoryPath.toString().replace("\\", "/"));
        model.addAttribute("parentPath", parentPath);

        return "upload";
    }
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   HttpServletRequest request,
                                   Model model) throws IOException {

        if (file.isEmpty()) {

            model.addAttribute("errorMessage", "Please select a file to upload");
            return "upload";
        }

        // 파일 정보 추출
        String originalName = file.getOriginalFilename();
        if (originalName == null) return "upload";
        String extension = getFileExtension(originalName);
        long fileSize = file.getSize();

        // 저장할 경로 설정
        String storedDir = fileDir + "/files/" + LocalDate.now().toString() + "/";
        String uuid = UUID.randomUUID().toString();
        String storedUrl = storedDir + uuid + '.' + extension;

        // 파일 저장.
        File destDir = new File(storedDir);
        if (!destDir.exists()) {

            destDir.mkdirs();
        }
        file.transferTo(new File(storedUrl));

        // 파일 업로드 정보 저장
        FileInfo uploadInfo = FileInfo.builder()
                .originalName(originalName)
                .extension(extension)
                .fileSize(fileSize)
                .storedUrl(storedUrl)
                .uploaderIpAddress(request.getRemoteAddr())
                .build();

        fileInfoRepository.save(uploadInfo);

        model.addAttribute("message", "File uploaded successfully");
        return "upload";
    }

    private String getFileExtension(String fileName) {

        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {

            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {

            return "";
        }
    }
}
