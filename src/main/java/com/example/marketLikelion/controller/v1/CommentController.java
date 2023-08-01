package com.example.marketLikelion.controller.v1;

import com.example.marketLikelion.dto.request.CommentRequestDto;
import com.example.marketLikelion.dto.response.CommentResponseDto;
import com.example.marketLikelion.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items/{itemId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Map<String, String>> registerComment(@PathVariable Long itemId,
                                                               @RequestBody CommentRequestDto requestDto) {

        commentService.registerComment(itemId, requestDto);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "댓글이 등록되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<CommentResponseDto>> getComments(@PathVariable Long itemId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<CommentResponseDto> comments = commentService.getComments(itemId, pageable);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Map<String, String>> updateComment(@PathVariable Long itemId,
                                                             @PathVariable Long commentId,
                                                             @RequestBody CommentRequestDto requestDto) {
        commentService.updateComment(itemId, commentId, requestDto);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "댓글이 수정되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PutMapping("/{commentId}/reply")
    public ResponseEntity<Map<String, String>> registerReply(@PathVariable Long itemId,
                                                             @PathVariable Long commentId,
                                                             @RequestBody CommentRequestDto requestDto) {
        commentService.registerReply(itemId, commentId, requestDto);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "댓글에 답변이 추가되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, String>> deleteComment(@PathVariable Long itemId,
                                                             @PathVariable Long commentId,
                                                             @RequestBody CommentRequestDto requestDto) {
        commentService.deleteComment(itemId, commentId, requestDto);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "댓글을 삭제했습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
