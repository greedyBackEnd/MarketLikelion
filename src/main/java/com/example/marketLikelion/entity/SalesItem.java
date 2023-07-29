package com.example.marketLikelion.entity;

import com.example.marketLikelion.entity.enumStatus.ItemStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Table(name = "sales_item")
public class SalesItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    private String imageUrl;

    @Column(nullable = false)
    private int minPriceWanted;

    private String status = ItemStatus.ON_SALE.getStatus();

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "salesItem", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "salesItem", cascade = CascadeType.ALL)
    private List<Negotiation> negotiations;

    public void update(SalesItem salesItemUpdate) {
        this.title = salesItemUpdate.title;
        this.description = salesItemUpdate.description;
        this.minPriceWanted = salesItemUpdate.minPriceWanted;
        this.writer = salesItemUpdate.writer;
    }

    public void updateImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateStatus(String status) {
        this.status = status;
    }
}
