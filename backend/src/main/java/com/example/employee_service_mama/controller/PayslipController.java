package com.example.employee_service_mama.controller;

import com.example.employee_service_mama.dto.PayslipDto;
import com.example.employee_service_mama.service.PayslipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/payslips")
@RequiredArgsConstructor
@CrossOrigin(
        origins = {
                "https://behara1.xyz",
                "http://behara1.xyz",
                "http://52.202.113.154:80",
                "http://127.0.0.1:5173",
                "http://localhost:5173"
        },
        allowCredentials = "true"
)
public class PayslipController {

    private final PayslipService service;

    // -------- BULK UPLOAD --------
    @PostMapping("/upload/bulk")
    public ResponseEntity<String> uploadBulk(
            @RequestParam List<MultipartFile> files,
            @RequestParam List<String> empids,
            @RequestParam Integer month,
            @RequestParam Integer year
    ) {

        if (files.size() != empids.size()) {
            return ResponseEntity.badRequest()
                    .body("Files & empids count mismatch");
        }

        for (int i = 0; i < files.size(); i++) {
            service.uploadAsync(empids.get(i), month, year, files.get(i));
        }

        return ResponseEntity.accepted()
                .body("Payslip upload started in background");
    }

    // -------- LIST PAYSLIPS --------
    @GetMapping("/empid/{empid}")
    public ResponseEntity<List<PayslipDto>> getPayslips(@PathVariable String empid) {
        return ResponseEntity.ok(service.getPayslipDtos(empid));
    }

    // -------- DOWNLOAD --------
    @GetMapping("/download/{id}")
    public ResponseEntity<String> download(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getUrlById(id));
    }
}
