package com.library.parkee.service;

import com.library.parkee.model.*;
import com.library.parkee.repository.BorrowRecordRepository;
import com.library.parkee.repository.BookRepository;
import com.library.parkee.repository.BorrowerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BorrowRecordService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;

    public BorrowRecordService(BorrowRecordRepository borrowRecordRepository, BookRepository bookRepository, BorrowerRepository borrowerRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
    }

    public BorrowRecord borrowBook(Long bookId, Long borrowerId, LocalDateTime dueDate) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new EntityNotFoundException("Borrower not found"));
        if (borrowRecordRepository.findByBookIdAndStatus(bookId, BorrowStatus.BORROWED).isPresent()) {
            throw new IllegalStateException("Book is not available");
        }
        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setBorrower(borrower);
        record.setDueDate(dueDate);
        record.setStatus(BorrowStatus.BORROWED);
        return borrowRecordRepository.save(record);
    }

    public BorrowRecord returnBook(Long borrowRecordId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new EntityNotFoundException("Borrow record not found"));
        if (record.getStatus() == BorrowStatus.RETURNED) {
            throw new IllegalStateException("Book is already returned");
        }
        record.setReturnDate(LocalDateTime.now());
        record.setStatus(record.getReturnDate().isAfter(record.getDueDate()) ? BorrowStatus.LATE : BorrowStatus.RETURNED);
        return borrowRecordRepository.save(record);
    }

    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordRepository.findAll();
    }

    public List<BorrowRecord> getOverdueRecords() {
        return borrowRecordRepository.findByStatus(BorrowStatus.LATE);
    }
}
