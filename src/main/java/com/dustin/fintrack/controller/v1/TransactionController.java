package com.dustin.fintrack.controller.v1;

import com.dustin.fintrack.dto.v1.request.TransactionFilterDTO;
import com.dustin.fintrack.dto.v1.request.TransactionRequestDTO;
import com.dustin.fintrack.dto.v1.response.DashboardResponseDTO;
import com.dustin.fintrack.dto.v1.response.TransactionResponseDTO;
import com.dustin.fintrack.model.User;
import com.dustin.fintrack.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> create(
            @Valid @RequestBody TransactionRequestDTO request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.create(request, user));
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDTO>> listAll(
            TransactionFilterDTO filter,
            @PageableDefault(size = 20, sort = "date", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(transactionService.listAllPaged(filter, pageable, user));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponseDTO> getDashboard(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(transactionService.getDashboardData(start, end, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> findById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(transactionService.findById(id, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequestDTO request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(transactionService.update(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        transactionService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}