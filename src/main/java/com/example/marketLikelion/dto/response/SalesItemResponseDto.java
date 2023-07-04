package com.example.marketLikelion.dto.response;

import com.example.marketLikelion.entity.SalesItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SalesItemResponseDto {
    private Long id;
    private String title;
    private String description;
    private int minPriceWanted;
    private String status;
    private String imageUrl;

    public static SalesItemResponseDto of(SalesItem salesItem) {
        return SalesItemResponseDto.builder()
                .id(salesItem.getId())
                .title(salesItem.getTitle())
                .description(salesItem.getDescription())
                .minPriceWanted(salesItem.getMinPriceWanted())
                .status(salesItem.getStatus())
                .imageUrl(salesItem.getImageUrl())
                .build();
    }
}
