package com.ernyka.cardmanager.repository;

import com.ernyka.cardmanager.dto.CardTransactionDTO;
import com.ernyka.cardmanager.model.CardTransaction;
import com.ernyka.cardmanager.model.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CardTransactionRepository extends JpaRepository<CardTransaction, Long> {
    Page<CardTransactionDTO> getAllByCardFrom_Uuid(String fromUuid, Pageable pageable);

    Page<CardTransactionDTO> getAllByCardTo_Uuid(String toUuid, Pageable pageable);

    Page<CardTransactionDTO> getAllByCardFrom_UuidAndCardTo_Uuid(String fromUuid, String toUuid, Pageable pageable);

    Page<CardTransactionDTO> getAllByCreatedDateBetween(Date from, Date to, Pageable pageable);

    Page<CardTransactionDTO> getAllByCreatedDateBefore(Date date, Pageable pageable);

    Page<CardTransactionDTO> getAllByCreatedDateAfter(Date date, Pageable pageable);


    Page<CardTransactionDTO> getAllByStatusIs(TransactionStatus status, Pageable pageable);

    Page<CardTransactionDTO> getAllByStatusNotAndStatusNotNull(TransactionStatus status, Pageable pageable);

    CardTransactionDTO getByIdIs(Long id);
}
