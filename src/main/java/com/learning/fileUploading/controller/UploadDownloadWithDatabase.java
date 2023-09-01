package com.learning.fileUploading.controller;

import com.learning.fileUploading.dto.FileDocument;
import com.learning.fileUploading.dto.FileUploadResponse;
import com.learning.fileUploading.service.DocFileDao;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.sound.midi.Soundbank;
import java.io.IOException;


@RestController
public class UploadDownloadWithDatabase
{
   private DocFileDao docFileDao;

    public UploadDownloadWithDatabase(DocFileDao docFileDao) {
        this.docFileDao = docFileDao;
    }
  @PostMapping("single/uploadDB")
    FileUploadResponse singleFileuploadToDb(@RequestParam("file")MultipartFile file) throws IOException {
      /*String filename = docFileDao.storeFile(file);

      String url = ServletUriComponentsBuilder.fromCurrentContextPath()
              .path("/download")
              .path(filename)
              .toUriString();

      String contentType = file.getContentType();
      FileUploadResponse response = new FileUploadResponse(filename,contentType,url);
*/
      System.out.println("enter into Method");
      String name = StringUtils.cleanPath(file.getOriginalFilename());
      System.out.println(name);
      FileDocument fileDocument = new FileDocument();
      fileDocument.setFileName(name);
      fileDocument.setDocFile(file.getBytes());
      System.out.println(fileDocument.toString());
      docFileDao.save(fileDocument);

      String url = ServletUriComponentsBuilder.fromCurrentContextPath()
              .path("/downloadFromDB/")
              .path(name)
              .toUriString();

      System.out.println(url);

      String contentType = file.getContentType();
      System.out.println(contentType);
      FileUploadResponse response = new FileUploadResponse(name, contentType,url);
      System.out.println(response);
      return response;
  }

  @GetMapping("/downloadFromDB/{fileName}")
    ResponseEntity<byte[]> downloadFileFromDB(@PathVariable String fileName , HttpServletRequest request)
  {
      FileDocument file = docFileDao.findByFileName(fileName);

      String mimeType = request.getServletContext().getMimeType(file.getFileName());

      return ResponseEntity.ok()
              .contentType(MediaType.parseMediaType(mimeType))
              .header(HttpHeaders.CONTENT_DISPOSITION,"inline;fileName =" + file.getFileName() )
              .body(file.getDocFile());
  }
}
