package com.example.fileupload;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class FileUploadApplication {

	public static void main(String[] args) {

		SpringApplication.run(FileUploadApplication.class, args);
	}

	@PostConstruct
	void started() {

		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

}
