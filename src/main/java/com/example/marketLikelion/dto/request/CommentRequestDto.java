package com.example.marketLikelion.dto.request;

import com.example.marketLikelion.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentRequestDto {

    private String writer;
    private String password;
    private String content;
    private String reply;

    public Comment toEntity() {
        return Comment.builder()
                .writer(writer)
                .password(password)
                .content(content)
                .reply(reply)
                .build();
    }
}
