package com.paccy.templates.springboot.services;

import com.paccy.templates.springboot.entities.File;
import com.paccy.templates.springboot.exceptions.InvalidFileException;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface IFileService {

    File getById(UUID id);

    File create(MultipartFile document, String directory);

    boolean delete(UUID id);

    String getFileExtension(String fileName);

    String handleFileName(String fileName, UUID id) throws InvalidFileException;

    boolean isValidExtension(String fileName) throws InvalidFileException;

}
