package com.example.marketLikelion.dto.response;

import com.example.marketLikelion.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {
    private Long id;
    private String writer;
    private String password;
    private String content;
    private String reply;

    public static CommentResponseDto of(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .writer(comment.getWriter())
                .password(comment.getPassword())
                .content(comment.getContent())
                .reply(comment.getReply())
                .build();
    }
}
