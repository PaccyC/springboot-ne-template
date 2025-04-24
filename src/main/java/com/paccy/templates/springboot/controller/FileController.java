package com.paccy.templates.springboot.controller;



import com.paccy.templates.springboot.entities.File;
import com.paccy.templates.springboot.services.IFileService;
import com.paccy.templates.springboot.services.impl.FileServiceImpl;
import com.paccy.templates.springboot.standalone.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;
    private final FileServiceImpl fileService;

//    @Value("${uploads.directory.user_profiles}")
    private String directory;

    @GetMapping("/load-file/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> loadProfileImage(@PathVariable String filename) {

        Resource file = this.fileStorageService.load(directory, filename);
        File _file = this.fileService.getByName(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, _file.getType())
                .body(file);
    }

}
