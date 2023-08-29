package com.learning.fileUploading.controller;

import com.learning.fileUploading.dto.FileUploadResponse;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class UploadDownloadWithFileSystemController
{
    private com.learning.fileUploading.service.fileStorageService fileStorageService;
    public UploadDownloadWithFileSystemController(com.learning.fileUploading.service.fileStorageService fileStorageService) {

        this.fileStorageService = fileStorageService;
    }

    @PostMapping("single/upload")
FileUploadResponse singleFileUpload(@RequestParam("file") MultipartFile file)
    {
        String fileName = fileStorageService.storeFile(file); // to store the file
        System.out.println(fileName.toString());
//making url
// from currentcontexctPath what server is returning it is goingb to return that context path
     // append the download
     //append the fileName
        //convert into URL
     //http://localhost:8081/download/fileName

        String url= ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileName).toUriString();
        System.out.println(url.toString());
        String contentType = file.getContentType();
        System.out.println(contentType.toString());
        FileUploadResponse response = new FileUploadResponse(fileName,contentType,url );
        System.out.println(response.toString());
        return response;
        //complete object reposnse entity is needed

    }
    @GetMapping("/download/{fileName}")
    ResponseEntity<org.springframework.core.io.Resource> downloadSingleFile(@PathVariable String fileName,HttpServletRequest httpRequest)
    {
        org.springframework.core.io.Resource resource = fileStorageService.downloadFile(fileName);

        System.out.println(resource.toString());

        String fileExtension = fileName.substring(fileName.lastIndexOf('.'));

        System.out.println("file extension" + fileExtension);

        MediaType contentType;
        switch (fileExtension.toLowerCase())
        {
            case "pdf"-> contentType = MediaType.APPLICATION_PDF;
            case "jpg,png,jpeg" -> contentType = MediaType.IMAGE_JPEG;
            case "xlsx" -> contentType = MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            case "xls" -> contentType = MediaType.valueOf("application/vnd.ms-excel");
            case "xml" -> contentType = MediaType.APPLICATION_XML;
            default -> contentType = MediaType.APPLICATION_OCTET_STREAM;
        }
//
//        try
//        {
//            mimeType = request.getServletPath().(resource.getFile().getAbsoluteFile());
//        }

        return  ResponseEntity.ok()
                .contentType(contentType)
               // .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;fileName="+resource.getFilename())
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline;fileName="+resource.getFilename())
                .body(resource);
    }

    @PostMapping("multiple/upload")
    List<FileUploadResponse> multipleUpload(@RequestParam("files") MultipartFile[] files)
    {
        System.out.println("enter in to controller");
        if(files.length > 7)
        {
            throw new RuntimeException("Too Many Files");
        }
        List<FileUploadResponse> uploadedFileList = new ArrayList<>();
        System.out.println("list declartion completed");
        Arrays.asList(files)
                .stream()
                .forEach( file ->{
                    String fileName = fileStorageService.storeFile(file);
                    String Url = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/download/")
                            .path(fileName)
                            .toUriString();
                    String contentType = file.getContentType();

                    FileUploadResponse response = new FileUploadResponse(fileName,contentType,Url);
                    System.out.println(response.toString());
                    uploadedFileList.add(response);
                });
        return uploadedFileList;
    }
    @GetMapping("zipDownload")
    void zipDownload(@RequestParam("fileName") String[] fileNames, HttpServletResponse response) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {

            Arrays.asList(fileNames)
                    .stream()
                    .forEach(file -> {
                        Resource resource = fileStorageService.downloadFile(file);
                        ZipEntry zipEntry = new ZipEntry(resource.getFilename());

                        try {
                            zipEntry.setSize(resource.contentLength());
                            zos.putNextEntry(zipEntry);

                            StreamUtils.copy(resource.getInputStream(), zos);
                            zos.closeEntry();

                        } catch (IOException e) {
                            System.out.println("exception while Zipping");
                        }
                    });
            zos.finish();
        }
        response.setStatus(200);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName = zipfile");
    }
    }

