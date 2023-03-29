package com.filemanager.file;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    @Autowired
    FileRepository fileRepository;

    public FileModel upload(MultipartFile file) throws IOException {
        String name = StringUtils.cleanPath(file.getOriginalFilename());
        String mainField = "";
        String secondaryField = "";
        String registrationNumber = "";
        Date numberDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date yearSystem = new Date(System.currentTimeMillis());
        String upDate = format.format(yearSystem);
        String type = file.getContentType().replaceAll("aplication/", "").toUpperCase();

        byte[] data = file.getBytes();
        FileModel fileDb = new FileModel(name, mainField, secondaryField, registrationNumber, numberDate, upDate, type, data);
        return fileDb;
    }

    public FileModel getFilebyId(Long id) {
        return fileRepository.getFileModelById(id);
    }

    public List<FileModel> searchByFields(String mainFieldOfInterest, String secondaryFieldOfInterest, String registrationNumber) {
        return fileRepository.searchByFields(mainFieldOfInterest, secondaryFieldOfInterest, registrationNumber);
    }
}
