package com.library.parkee.controller;

import com.library.parkee.model.BorrowRecord;
import com.library.parkee.service.BorrowRecordService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/borrow-records")
public class BorrowRecordController {

    private final BorrowRecordService borrowRecordService;

    public BorrowRecordController(BorrowRecordService borrowRecordService) {
        this.borrowRecordService = borrowRecordService;
    }

    @GetMapping
    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordService.getAllBorrowRecords();
    }

    @PostMapping("/borrow")
    public ResponseEntity<BorrowRecord> borrowBook(@Valid @RequestBody BorrowRequest request) {
        BorrowRecord record = borrowRecordService.borrowBook(request.getBookId(), request.getBorrowerId(), request.getDueDate());
        return ResponseEntity.ok(record);
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<BorrowRecord> returnBook(@PathVariable Long id) {
        BorrowRecord record = borrowRecordService.returnBook(id);
        return ResponseEntity.ok(record);
    }

    @GetMapping("/overdue")
    public List<BorrowRecord> getOverdueRecords() {
        return borrowRecordService.getOverdueRecords();
    }

    public static class BorrowRequest {
        @NotNull
        private Long bookId;
        @NotNull
        private Long borrowerId;
        @NotNull
        private LocalDateTime dueDate;

        public Long getBookId() {
            return bookId;
        }

        public void setBookId(Long bookId) {
            this.bookId = bookId;
        }

        public Long getBorrowerId() {
            return borrowerId;
        }

        public void setBorrowerId(Long borrowerId) {
            this.borrowerId = borrowerId;
        }

        public LocalDateTime getDueDate() {
            return dueDate;
        }

        public void setDueDate(LocalDateTime dueDate) {
            this.dueDate = dueDate;
        }
    }
}

