package com.library.parkee.repository;

import com.library.parkee.model.BorrowRecord;
import com.library.parkee.model.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByStatus(BorrowStatus status);
    Optional<BorrowRecord> findByBookIdAndStatus(Long bookId, BorrowStatus status);
}