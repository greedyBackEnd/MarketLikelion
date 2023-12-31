package com.example.marketLikelion.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String phone;

    private String email;

    private String address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SalesItem> salesItems;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Negotiation> negotiations;

    public void updateUser(User userUpdate) {
        if (userUpdate.getPassword() != null) {
            this.password = userUpdate.getPassword();
        }
        this.phone = userUpdate.getPhone();
        this.email = userUpdate.getEmail();
        this.address = userUpdate.getAddress();
    }
}