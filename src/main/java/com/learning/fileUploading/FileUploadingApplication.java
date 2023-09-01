package com.learning.fileUploading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class FileUploadingApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileUploadingApplication.class, args);
	}

}
