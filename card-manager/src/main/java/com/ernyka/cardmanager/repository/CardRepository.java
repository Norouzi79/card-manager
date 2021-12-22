package com.ernyka.cardmanager.repository;

import com.ernyka.cardmanager.dto.CardDTO;
import com.ernyka.cardmanager.model.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Page<CardDTO> getAllByUuidNotNullAndIsDeletedFalse(Pageable pageable);

    CardDTO getByUuidEquals(String uuid);

    CardDTO getByIdEquals(Long id);

    Card getByUuidEqualsAndIsDeletedFalse(String uuid);
}
