package com.dustin.fintrack.controller.v1;

import com.dustin.fintrack.dto.v1.request.TransactionRequestDTO;
import com.dustin.fintrack.dto.v1.response.DashboardResponseDTO;
import com.dustin.fintrack.dto.v1.response.TransactionResponseDTO;
import com.dustin.fintrack.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> create(@Valid @RequestBody TransactionRequestDTO request) {
        TransactionResponseDTO response = transactionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> listAll() {
        List<TransactionResponseDTO> transactions = transactionService.listAll();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponseDTO> getDashboard(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        DashboardResponseDTO dashboardData = transactionService.getDashboardData(start, end);

        return ResponseEntity.ok(dashboardData);
    }
}