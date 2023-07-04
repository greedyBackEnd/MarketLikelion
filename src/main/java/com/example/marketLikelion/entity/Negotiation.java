package com.example.marketLikelion.entity;

import com.example.marketLikelion.entity.enumStatus.NegotiationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Table(name = "negotiation")
public class Negotiation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salesItemId")
    private SalesItem salesItem;

    @Column(nullable = false)
    private int suggestedPrice;

    private String status = NegotiationStatus.SUGGESTED.getStatus();

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String password;

    public void updateSuggestedPrice(int suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
    }

    public void updateStatus(String status) {
        this.status = status;
    }
}
