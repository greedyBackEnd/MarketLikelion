package com.example.marketLikelion.entity.enumStatus;

import lombok.Getter;

@Getter
public enum NegotiationStatus {
    SUGGESTED("제안"),
    ACCEPTED("수락"),
    REJECTED("거절"),
    CONFIRMED("확정");

    private final String status;

    NegotiationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
