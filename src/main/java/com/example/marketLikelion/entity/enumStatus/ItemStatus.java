package com.example.marketLikelion.entity.enumStatus;

import lombok.Getter;

@Getter
public enum ItemStatus {
    ON_SALE("판매중"),
    SOLD_OUT("판매 완료");

    private final String status;

    ItemStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
