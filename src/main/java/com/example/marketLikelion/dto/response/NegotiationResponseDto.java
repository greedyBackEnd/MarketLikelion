package com.example.marketLikelion.dto.response;

import com.example.marketLikelion.entity.Negotiation;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NegotiationResponseDto {
    private Long id;
    private int suggestedPrice;
    private String status;

    public static NegotiationResponseDto of(Negotiation negotiation) {
        return NegotiationResponseDto.builder()
                .id(negotiation.getId())
                .suggestedPrice(negotiation.getSuggestedPrice())
                .status(negotiation.getStatus())
                .build();
    }
}
