package com.example.marketLikelion.repository;

import com.example.marketLikelion.entity.Negotiation;
import com.example.marketLikelion.entity.SalesItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NegotiationRepository extends JpaRepository<Negotiation, Long> {
    Page<Negotiation> findBySalesItem(SalesItem salesItem, Pageable pageable);
    Page<Negotiation> findBySalesItemAndWriterAndPassword(SalesItem salesItem, String writer, String password, Pageable pageable);
    List<Negotiation> findBySalesItemAndIdNot(SalesItem salesItem, Long id);
}
