package com.example.fileupload.repository;

import com.example.fileupload.domain.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
    Optional<FileInfo> findByStoredUrlContaining (String storedUrl);
}
