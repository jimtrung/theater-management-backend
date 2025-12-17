package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.dto.BillRequest;
import com.github.jimtrung.theater.service.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bills")
//nối đến hàm tạo hóa đơn
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }
//hàm tạo hóa đơn
    @PostMapping("/{userId}")
    public ResponseEntity<String> createBill(
            @PathVariable UUID userId,
            @RequestBody BillRequest request
    ) throws Exception{
        // Gọi service để gửi mail và xử lý logic hóa đơn
        billService.bill(userId, request);
        //thông báo
        return ResponseEntity.ok("Hóa đơn đã được xử lý và gửi tới email: " + request.to());
    }
}