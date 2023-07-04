package com.example.marketLikelion.dto.request;

import com.example.marketLikelion.entity.SalesItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SalesItemRequestDto {
    private String title;
    private String description;
    private int minPriceWanted;
    private String writer;
    private String password;

    public SalesItem toEntity() {
        return SalesItem.builder()
                .title(title)
                .description(description)
                .minPriceWanted(minPriceWanted)
                .writer(writer)
                .password(password)
                .build();
    }
}
