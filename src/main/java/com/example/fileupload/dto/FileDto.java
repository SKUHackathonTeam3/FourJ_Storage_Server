package com.example.fileupload.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class FileDto {

    private String originalName;
    private String name;
    private boolean isDirectory;
    private String path;
    private LocalDateTime uploadedAt;
}