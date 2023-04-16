package com.filemanager.file;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/api/v1/file")
public class FileController {
    @Autowired
    FileService fileService;


    @Autowired
    FileRepository fileRepository;


    @PostMapping("/upload")
    public void upload(@RequestParam MultipartFile fileUpload,
                       @RequestParam("mainFieldOfInterest") String main,
                       @RequestParam("secondaryFieldOfInterest") String second,
                       @RequestParam("registrationNumber") String rNum,
                       @RequestParam("numberDate") String numberDate
    ) throws IOException, ParseException {;
        FileModel fileToSave = fileService.upload(fileUpload);
        fileToSave.setMainFieldOfInterest(main);
        fileToSave.setSecondaryFieldOfInterest(second);
        fileToSave.setRegistrationNumber(rNum);
        String format = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate date = LocalDate.parse(numberDate, formatter);
        fileToSave.setNumberDate(date);
        fileRepository.save(fileToSave);

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
            String format = "yyyy-MM-dd";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            haleluiaDate = LocalDate.parse(numberDate, formatter);
        }
        list = fileService.searchByFields(mainField, secondField, numberField, haleluiaDate);
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


