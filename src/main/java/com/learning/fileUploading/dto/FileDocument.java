package com.learning.fileUploading.dto;

import jakarta.persistence.*;
import jdk.jfr.Enabled;

import java.util.Arrays;
@Entity
public class FileDocument {

    @Id
  //  @SequenceGenerator(name = "file_document_seq_gen",sequenceName = "ebs.file_document", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "fileName")
    private String fileName;
    @Column(name = "docfile")
    @Lob
    private byte[] docFile;
    @Override
    public String toString() {
        return "FileDocument{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", docFile=" + Arrays.toString(docFile) +
                '}';
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getDocFile() {
        return docFile;
    }

    public void setDocFile(byte[] docFile) {
        this.docFile = docFile;
    }
}
