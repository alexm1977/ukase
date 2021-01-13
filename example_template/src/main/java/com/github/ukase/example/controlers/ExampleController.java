package com.github.ukase.example.controlers;

import com.github.ukase.example.services.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
public class ExampleController {

    private final ReportService reportService;

    public ExampleController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> getReport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var file = reportService.createPdf();

        byte[] contents = FileUtils.readFileToByteArray(file);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = file.getName();
        headers.setContentDispositionFormData(filename, filename);
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

}
