package com.example.marketLikelion.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salesItemId")
    private SalesItem salesItem;

//    @Column(nullable = false)
    private String writer;

//    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String content;

    private String reply;

    public void updateComment(Comment commentUpdate) {
        this.writer = commentUpdate.getWriter();
        this.password = commentUpdate.getPassword();
        this.content = commentUpdate.getContent();
    }

    public void registerReply(String reply) {
        this.reply = reply;
    }
}
