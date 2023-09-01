package com.learning.fileUploading.service;

import com.learning.fileUploading.dto.FileDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Repository
public interface DocFileDao extends CrudRepository<FileDocument,Long>
{
    FileDocument findByFileName(String fileName);
}
