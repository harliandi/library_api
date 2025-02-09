package com.library.parkee.service;

import com.library.parkee.model.Book;
import com.library.parkee.model.BorrowRecord;
import com.library.parkee.model.BorrowStatus;
import com.library.parkee.model.Borrower;
import com.library.parkee.repository.BorrowRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowServiceTest {

    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @InjectMocks
    private BorrowRecordService borrowService;

    private BorrowRecord borrowRecord;
    private Book book;
    private Borrower borrower;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        borrower = new Borrower();
        borrower.setId(1L);
        borrower.setName("John Doe");

        borrowRecord = new BorrowRecord();
        borrowRecord.setId(1L);
        borrowRecord.setBook(book);
        borrowRecord.setBorrower(borrower);
        borrowRecord.setBorrowDate(LocalDateTime.now());
        borrowRecord.setDueDate(LocalDateTime.now().plusDays(7));
    }

    @Test
    void testReturnBookOnTime() {
        borrowRecord.setReturnDate(LocalDateTime.now().plusDays(5));

        when(borrowRecordRepository.findById(1L)).thenReturn(Optional.of(borrowRecord));

        borrowService.returnBook(1L);

        assertEquals(BorrowStatus.RETURNED, borrowRecord.getStatus());
        verify(borrowRecordRepository, times(1)).save(borrowRecord);
    }

    @Test
    void testReturnBookLate() {
        borrowRecord.setDueDate(LocalDateTime.now().minusDays(3));
        borrowRecord.setStatus(BorrowStatus.BORROWED);

        when(borrowRecordRepository.findById(1L)).thenReturn(Optional.of(borrowRecord));

        borrowService.returnBook(1L);

        assertEquals(BorrowStatus.LATE, borrowRecord.getStatus());
    }
}