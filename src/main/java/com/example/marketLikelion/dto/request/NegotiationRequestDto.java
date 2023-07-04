package com.example.marketLikelion.dto.request;

import com.example.marketLikelion.entity.Negotiation;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NegotiationRequestDto {

    private String writer;
    private String password;
    private Integer suggestedPrice;
    private String status;

    public Negotiation toEntity() {
        return Negotiation.builder()
                .writer(writer)
                .password(password)
                .suggestedPrice(suggestedPrice)
                .status(status)
                .build();
    }
}
