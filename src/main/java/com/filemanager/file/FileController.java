package com.filemanager.file;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.http.HttpClient;
import java.util.List;

@RestController
public class FileController {
    @Autowired
    FileService fileService;

    @Autowired
    FileRepository fileRepository;

    @PostMapping("/upload")
    public FileModel upload (@RequestParam String main, @RequestParam String secondary, @RequestParam(required = false) String registrationNumber, @RequestParam("file") MultipartFile file) throws IOException{
        FileModel fileToSave = fileService.upload(file);
        fileToSave.setMainFieldOfInterest(main);
        fileToSave.setSecondaryFieldOfInterest(secondary);
        fileToSave.setRegistrationNumber(registrationNumber);
        return fileRepository.save(fileToSave);


    }

    @GetMapping(value = "/allFiles")
    public ModelAndView files(){
        ModelAndView mav = new ModelAndView("files");
        List<FileModel> list = fileRepository.findAll();
        mav.addObject("files", list);
        return mav;
    }

    @GetMapping("/addFile")
    public ModelAndView addFile(){
        ModelAndView mav = new ModelAndView("addFile");
        FileModel fileToSave = new FileModel();
        String name = "";
        mav.addObject("file-model", fileToSave);
        return mav;
    }
    @PostMapping("/saveFile")
    public void saveFile(@RequestParam("file") MultipartFile file, @RequestParam("mainFieldOfInterest") String mainFieldOfInterest, @RequestParam("secondaryFieldOfInterest") String secondaryFieldOfInterest, @RequestParam("registrationNumber") String registrationNumber, HttpServletResponse response) throws IOException {
        FileModel fileUpload = fileService.upload(file);
        if(!file.isEmpty()){
             try{
                byte[] bytes = file.getBytes();
                fileUpload.setData(bytes);
                fileUpload.setMainFieldOfInterest(mainFieldOfInterest);
                fileUpload.setSecondaryFieldOfInterest(secondaryFieldOfInterest);
                fileUpload.setRegistrationNumber(registrationNumber);
            } catch (Exception e){

             }
        }
        fileRepository.save(fileUpload);
        response.sendRedirect("/allFiles");

    }

    @GetMapping("/download/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> downloadFile (@PathVariable Long id){
    byte[] data = fileService.getFilebyId(id).getData();
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileService.getFilebyId(id).getName()+ "\"")
            .body(data);

    }

    @GetMapping("/junk/{id}")
    public void deleteFile (@PathVariable Long id, HttpServletResponse response) throws IOException {
        fileRepository.deleteById(id);
        response.sendRedirect("/allFiles");
    }

}
