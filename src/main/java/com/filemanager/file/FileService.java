package com.filemanager.file;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class FileService {
    @Autowired
    FileRepository fileRepository;

    public FileModel upload(MultipartFile fileUpload) throws IOException, ParseException {
        String name = "";
        if(!StringUtils.cleanPath(Objects.requireNonNull(fileUpload.getOriginalFilename())).isEmpty()){
            name = StringUtils.cleanPath(Objects.requireNonNull(fileUpload.getOriginalFilename()));
        }
        String mainField = "";
        String secondaryField = "";
        String registrationNumber = "";
        LocalDate numberDate = null;
        LocalDate fDate = null;
        String fName = "";
        String fNumber = "";
        long fValue = 0;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date yearSystem = new Date(System.currentTimeMillis());
        String upDate = format.format(yearSystem);
        String type = fileUpload.getContentType().replaceAll("aplication/", "").toUpperCase();


        byte[] data = fileUpload.getBytes();
        FileModel fileDb = new FileModel(name, mainField, secondaryField, registrationNumber, numberDate, fDate, fName, fNumber, fValue, upDate, type, data);

        return fileDb;
    }

    public FileModel getFilebyId(Long id) {
        return fileRepository.getFileModelById(id);
    }

    public List<FileModel> searchByFields(String mainFieldOfInterest, String secondaryFieldOfInterest, String registrationNumber, LocalDate numberDate) {
        return fileRepository.searchByFields(mainFieldOfInterest, secondaryFieldOfInterest, registrationNumber, numberDate);
    }
}
