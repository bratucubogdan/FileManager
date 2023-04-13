package com.filemanager.file;

import com.filemanager.auth.AuthenticationController;
import com.filemanager.auth.Token;
import com.filemanager.security.AuthorizationHeaderInterceptor;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/v1/file")
public class FileController {
    @Autowired
    FileService fileService;

    @Autowired
    Token token;
    @Autowired
    AuthenticationController controller;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/upload")
    public void upload(@RequestParam String main, @RequestParam String secondary, @RequestParam(required = false) String registrationNumber, @DateTimeFormat(pattern = "dd/MM/yyyy") Date numberDate, @RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException, ParseException {
        AuthorizationHeaderInterceptor interceptor = new AuthorizationHeaderInterceptor(token.getToken());
        restTemplate.getInterceptors().add(interceptor);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8080/api/v1/file/upload", file, String.class);
        FileModel fileToSave = fileService.upload(file);
        fileToSave.setMainFieldOfInterest(main);
        fileToSave.setSecondaryFieldOfInterest(secondary);
        fileToSave.setRegistrationNumber(registrationNumber);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        fileRepository.save(fileToSave);
        response.sendRedirect("/api/v1/file/search");


    }

    @GetMapping(value = {"/search"})
    public ModelAndView files(
            @RequestParam(value = "mainFieldOfInterest", required = false) String mainFieldOfInterest,
            @RequestParam(value = "secondaryFieldOfInterest", required = false) String secondaryFieldOfInterest,
            @RequestParam(value = "numberDate", required = false) String numberDate
    ) throws ParseException {

        AuthorizationHeaderInterceptor interceptor = new AuthorizationHeaderInterceptor(token.getToken());
        restTemplate.getInterceptors().add(interceptor);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:8080/api/v1/file/search", String.class);

        ModelAndView mav = new ModelAndView("files");
        List<FileModel> list;
        String mainField = mainFieldOfInterest == "" ? null : mainFieldOfInterest;
        String secondField = secondaryFieldOfInterest == "" ? null : secondaryFieldOfInterest;
        Date haleluiaDate = null;
        if (numberDate != null && numberDate != "") {
            Date numberDateFormated = new SimpleDateFormat("yyyy-MM-dd").parse(numberDate);
            SimpleDateFormat goodDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String hope = goodDateFormat.format(numberDateFormated);
            haleluiaDate = goodDateFormat.parse(hope);
        }
        list = fileService.searchByFields(mainField, secondField, haleluiaDate);
        mav.addObject("files", list);
        return mav;

    }

    @GetMapping("/addFile")
    public ModelAndView addFile() {
        ModelAndView mav = new ModelAndView("addFile");
        FileModel fileToSave = new FileModel();
        mav.addObject("file-model", fileToSave);
        return mav;
    }

    @PostMapping("/saveFile")
    public void saveFile(@RequestParam("file") MultipartFile file,
                         @RequestParam("mainFieldOfInterest") String mainFieldOfInterest,
                         @RequestParam("secondaryFieldOfInterest") String secondaryFieldOfInterest,
                         @RequestParam("registrationNumber") String registrationNumber,
                         @RequestParam("numberDate") @DateTimeFormat(pattern = "MM/dd/yyyy") String numberDate,
                         HttpServletResponse response) throws IOException, ParseException {
        FileModel fileUpload = fileService.upload(file);
        Date numberDateFormated = new SimpleDateFormat("yyyy-MM-dd").parse(numberDate);
        SimpleDateFormat goodDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String hope = goodDateFormat.format(numberDateFormated);
        Date haleluiaDate = goodDateFormat.parse(hope);

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                fileUpload.setData(bytes);
                fileUpload.setMainFieldOfInterest(mainFieldOfInterest);
                fileUpload.setSecondaryFieldOfInterest(secondaryFieldOfInterest);
                fileUpload.setRegistrationNumber(registrationNumber);
                fileUpload.setNumberDate(haleluiaDate);


            } catch (Exception e) {

            }
        }
        fileRepository.save(fileUpload);
        response.sendRedirect("/api/v1/file/search");

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
        response.sendRedirect("/api/v1/file/search");
    }


//    public void testHeader3(final RestTemplate restTemplate) {
//        // Add a ClientHttpRequestInterceptor to the RestTemplate
//        restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
//            @Override
//            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
//                request.getHeaders().set("Authorization", "Bearer " + token.getToken()); // Set the header for each request
//                return execution.execute(request, body);
//            }
//        });
//
//
//        ResponseEntity<Map> response = restTemplate.getForEntity("https://httpbin.org/user-agent", Map.class);
//        System.out.println(response.getBody());
//
//        ResponseEntity<Map> response2 = restTemplate.getForEntity("https://httpbin.org/headers", Map.class);
//        System.out.println(response2.getBody());
//
//        // Add the header to another URL
//        ResponseEntity<Map> response3 = restTemplate.getForEntity("https://httpbin.org/get", Map.class);
//        System.out.println(response3.getBody());
//    }
}


