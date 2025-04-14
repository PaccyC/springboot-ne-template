package com.paccy.templates.springboot.services.impl;


import com.paccy.templates.springboot.entities.File;
import com.paccy.templates.springboot.enums.EFileSizeType;
import com.paccy.templates.springboot.enums.EFileStatus;
import com.paccy.templates.springboot.exceptions.AppException;
import com.paccy.templates.springboot.exceptions.BadRequestException;
import com.paccy.templates.springboot.exceptions.ResourceNotFoundException;
import com.paccy.templates.springboot.repository.FileRepository;
import com.paccy.templates.springboot.services.IFileService;
import com.paccy.templates.springboot.standalone.FileStorageService;
import com.paccy.templates.springboot.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements IFileService {


    private final FileRepository fileRepository;
    private final FileStorageService fileStorageService;

    @Value("${uploads.extensions}")
    private String extensions;

    @Override
    public File getById(UUID id) {
        return this.fileRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("File", "id", id.toString()));
    }

    @Override
    public File create(MultipartFile document, String directory) {
        File file = new File();
        file.setStatus(EFileStatus.PENDING);
        String fileName = FileUtil.generateUUID(Objects.requireNonNull(document.getOriginalFilename()));
        String updatedFileName = this.handleFileName(fileName, UUID.randomUUID());
        EFileSizeType sizeType = FileUtil.getFileSizeTypeFromFileSize(file.getSize());
        int size = FileUtil.getFormattedFileSizeFromFileSize(document.getSize(), sizeType);

        file.setName(updatedFileName);
        file.setPath(fileStorageService.save(document, directory, updatedFileName));
        file.setStatus(EFileStatus.SUCCESS);
        file.setType(document.getContentType());
        file.setSize(size);
        file.setSizeType(sizeType);

        return this.fileRepository.save(file);
    }

    @Override
    public boolean delete(UUID id) {
        boolean exists = this.fileRepository.existsById(id);
        if (!exists)
            throw new ResourceNotFoundException("File", "id", id.toString());
        this.fileStorageService.removeFileOnDisk(this.getById(id).getPath());
        this.fileRepository.deleteById(id);
        return true;
    }

    @Override
    public String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex < 0) {
            return null;
        }
        return fileName.substring(dotIndex + 1);
    }

    @Override
    public String handleFileName(String fileName, UUID id) {

        String cleanFileName = fileName.replaceAll("[^A-Za-z0-9.()]", "");
        String extension = getFileExtension(cleanFileName);

        if (!isValidExtension(cleanFileName)) {
            throw new BadRequestException("Invalid File Extension");
        }

        String base = "image-" + id;

        cleanFileName = base + "." + extension;

        return cleanFileName;
    }

    @Override
    public boolean isValidExtension(String fileName) {
        String fileExtension = getFileExtension(fileName);

        if (fileExtension == null) {
            throw new AppException("No File Extension");
        }

        fileExtension = fileExtension.toLowerCase();

        for (String validExtension : extensions.split(",")) {
            if (fileExtension.equals(validExtension)) {
                return true;
            }
        }
        return false;
    }


    public File getByName(String filename) {
        return this.fileRepository.findByName(filename);
    }

}
