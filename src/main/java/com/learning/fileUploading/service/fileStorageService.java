package com.learning.fileUploading.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class fileStorageService {

   private Path fileStoragePath;
   private String fileStorageLocation;

   //constructor
    public fileStorageService(@Value("${file.storage.location:temp}") String fileStorageLocation) {

       this.fileStorageLocation = fileStorageLocation;
        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        System.out.println("file storage path from service starting" +fileStoragePath.toString());
        try {
            Files.createDirectories(fileStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating file directory");
        }

    }

    //task of copying and uploading the file into system
    //returns the fileName
    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println("fileName: " +fileName.toString());
        Path filePath = Paths.get(fileStoragePath + "\\" + fileName);
        System.out.println("FilePath " + filePath.toString());
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Issue in storing the file");
        }

        return fileName;
    }

    public Resource downloadFile(String fileName) {

    Path path =    Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);
        System.out.println("Path from download " + path.toString());
        Resource resource;
        try {
             resource = new UrlResource(path.toUri());
            System.out.println("Resource " + resource.toString());

        } catch (MalformedURLException e) {
            throw new RuntimeException("Issue in reading the file");
        }

        if(resource.exists() || resource.isReadable())
        {
            return resource;
        }
        else {
            throw new RuntimeException("the file does not exist or not readable");
        }

    }
}
