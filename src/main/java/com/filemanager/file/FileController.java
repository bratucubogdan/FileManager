package com.filemanager.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://10.100.0.114:3000/")
@RequestMapping("/api/v1/file")
public class FileController {

    private final FileService fileService;
    @Autowired
    public  FileController (FileService fileService){
        this.fileService = fileService;
    }

    @Autowired
    FileRepository fileRepository;


    @PostMapping("/upload")
    public void upload(@RequestParam MultipartFile fileUpload,
                       @RequestParam("mainFieldOfInterest") String main,
                       @RequestParam("secondaryFieldOfInterest") String second,
                       @RequestParam("registrationNumber") String rNum,
                       @RequestParam("numberDate") String numberDate,
                       @RequestParam("fDate") String fDate,
                       @RequestParam("fName") String fName,
                       @RequestParam("fValue") String fValue,
                       @RequestParam("fNumber") String fNumber
    ) throws IOException, ParseException {;
        FileModel fileToSave = fileService.upload(fileUpload);
        fileToSave.setMainFieldOfInterest(main);
        fileToSave.setSecondaryFieldOfInterest(second);
        fileToSave.setRegistrationNumber(rNum);
        fileToSave.setfDate(LocalDate.parse(fDate));
        fileToSave.setfName(fName);
        fileToSave.setfNumber(fNumber);
        fileToSave.setfValue(Double.valueOf(fValue));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        String reformatDate = myFormat.format(format.parse(numberDate));
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate date = LocalDate.parse(reformatDate, formatter2);
        fileToSave.setNumberDate(date);
        fileRepository.save(fileToSave);
        System.out.println(reformatDate);

    }

    @GetMapping(value = {"/allFiles"})
    public ResponseEntity<List<FileModel>> files(
            @RequestParam(value = "mainSearch", required = false) String mainFieldOfInterest,
            @RequestParam(value = "secondarySearch", required = false) String secondaryFieldOfInterest,
            @RequestParam(value = "numberSearch", required = false) String registrationNumber,
            @RequestParam(value = "dateSearch", required = false) String numberDate)
             throws ParseException {
        List<FileModel> list;
        LocalDate haleluiaDate = null;
        String mainField = mainFieldOfInterest == "" ? null : mainFieldOfInterest;
        String secondField = secondaryFieldOfInterest == "" ? null : secondaryFieldOfInterest;
        String numberField = registrationNumber == "" ? null : registrationNumber;
        System.out.println(registrationNumber);
        if (numberDate != null && numberDate != "") {
            String format = "dd/MM/yyyy";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            haleluiaDate = LocalDate.parse(numberDate, formatter);
        }
        list = fileService.searchByFields(mainField, secondField, numberField, haleluiaDate);
        for(FileModel i: list){
            String format = "d/MM/yyyy";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            String k = i.getNumberDate().format(formatter);
            LocalDate goodformat = LocalDate.parse(k,formatter);
            i.setNumberDate(goodformat);
        }

        return ResponseEntity.ok(list);

    }

    @GetMapping("/download/{id}")
        public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
            FileModel file = fileService.getFilebyId(id);
            ByteArrayResource resource = new ByteArrayResource(file.getData());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, file.getType());
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        }

    @DeleteMapping("/junk/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Long id) throws IOException {
        fileRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }


}


