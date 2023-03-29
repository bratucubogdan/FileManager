package com.filemanager.file;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class FileController {
    @Autowired
    FileService fileService;

    @Autowired
    FileRepository fileRepository;

    @PostMapping("/upload")
    public void upload(@RequestParam String main, @RequestParam String secondary, @RequestParam(required = false) String registrationNumber, @DateTimeFormat(pattern = "dd/MM/yyyy") Date numberDate, @RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException, ParseException {
        FileModel fileToSave = fileService.upload(file);
        fileToSave.setMainFieldOfInterest(main);
        fileToSave.setSecondaryFieldOfInterest(secondary);
        fileToSave.setRegistrationNumber(registrationNumber);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        fileRepository.save(fileToSave);
        response.sendRedirect("/");


    }

    @RequestMapping(value = {"/search"})
    public ModelAndView files(
            @RequestParam(value = "mainFieldOfInterest", required = false) String mainFieldOfInterest,
            @RequestParam(value = "secondaryFieldOfInterest", required = false) String secondaryFieldOfInterest,
            @RequestParam(value = "registrationNumber", required = false) String registrationNumber
            ) {
        ModelAndView mav = new ModelAndView("files");
        List<FileModel> list;
        list = fileService.searchByFields(mainFieldOfInterest, secondaryFieldOfInterest, registrationNumber);
        mav.addObject("files", list);
        return mav;
    }

    @GetMapping("/addFile")
    public ModelAndView addFile() {
        ModelAndView mav = new ModelAndView("addFile");
        FileModel fileToSave = new FileModel();
        String name = "";
        mav.addObject("file-model", fileToSave);
        return mav;
    }

    @PostMapping("/saveFile")
    public void saveFile(@RequestParam("file") MultipartFile file, @RequestParam("mainFieldOfInterest") String mainFieldOfInterest, @RequestParam("secondaryFieldOfInterest") String secondaryFieldOfInterest, @RequestParam("registrationNumber") String registrationNumber, @RequestParam("numberDate") Date numberDate, HttpServletResponse response) throws IOException {
        FileModel fileUpload = fileService.upload(file);

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                fileUpload.setData(bytes);
                fileUpload.setMainFieldOfInterest(mainFieldOfInterest);
                fileUpload.setSecondaryFieldOfInterest(secondaryFieldOfInterest);
                fileUpload.setRegistrationNumber(registrationNumber);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                numberDate = formatter.parse(formatter.format(numberDate));
                fileUpload.setNumberDate(numberDate);
            } catch (Exception e) {

            }
        }
        fileRepository.save(fileUpload);
        response.sendRedirect("/");

    }

    @GetMapping("/download/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        byte[] data = fileService.getFilebyId(id).getData();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileService.getFilebyId(id).getName() + "\"")
                .body(data);

    }

    @GetMapping("/junk/{id}")
    public void deleteFile(@PathVariable Long id, HttpServletResponse response) throws IOException {
        fileRepository.deleteById(id);
        response.sendRedirect("/");
    }
//    @RequestMapping("/search")
//    public ModelAndView modelAndView(@RequestParam(name = "mainField", required = false) String mainField, @RequestParam(name = "secondaryField", required = false) String secondaryField, @RequestParam(name = "registrationNumber", required = false) String registrationNumber){
//        List<FileModel> listFilter= fileService.filter(mainField, secondaryField, registrationNumber);
//        ModelAndView modelAndView = new ModelAndView("search");
//        modelAndView.addObject("filesFilter", listFilter);
//        return modelAndView;
//    }


}


