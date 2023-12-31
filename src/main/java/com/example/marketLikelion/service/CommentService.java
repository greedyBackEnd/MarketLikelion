package com.example.marketLikelion.service;

import com.example.marketLikelion.dto.request.CommentRequestDto;
import com.example.marketLikelion.dto.response.CommentResponseDto;
import com.example.marketLikelion.entity.Comment;
import com.example.marketLikelion.entity.SalesItem;
import com.example.marketLikelion.repository.CommentRepository;
import com.example.marketLikelion.repository.SalesItemRepository;
import com.example.marketLikelion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final SalesItemRepository salesItemRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    // 댓글 등록 (유저 X -> V1)
    @Transactional
    public void registerComment(Long itemId, CommentRequestDto requestDto) {
        SalesItem salesItem = salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));

        Comment comment = requestDto.toEntity().toBuilder()
                .salesItem(salesItem)
                .build();
        commentRepository.save(comment);
    }

    // 댓글 등록 (유저 O -> V2)
    @Transactional
    public void registerComment(Long itemId, CommentRequestDto requestDto, String username) {
        SalesItem salesItem = salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));

        Comment comment = requestDto.toEntity().toBuilder()
                .salesItem(salesItem)
                .user(userRepository.findByUsername(username).get())
                .build();
        commentRepository.save(comment);
    }

    public Page<CommentResponseDto> getComments(Long itemId, Pageable pageable) {
        SalesItem salesItem = salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));

        return commentRepository.findBySalesItem(salesItem, pageable)
                .map(CommentResponseDto::of);
    }

    // 댓글 수정 (유저 X -> V1)
    @Transactional
    public void updateComment(Long itemId, Long commentId, CommentRequestDto requestDto) {
        salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        if (!comment.getPassword().equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        comment.updateComment(requestDto.toEntity());
    }

    // 댓글 수정 (유저 O -> V2)
    @Transactional
    public void updateComment(Long itemId, Long commentId, CommentRequestDto requestDto, String username) {
        salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        if (!comment.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("다른 유저가 작성한 댓글입니다.");
        }

        comment.updateComment(requestDto.toEntity());
    }

    // 답글 등록 (유저 X -> V1)
    @Transactional
    public void registerReply(Long itemId, Long commentId, CommentRequestDto requestDto) {
        SalesItem salesItem = salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 없습니다. id=" + itemId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + commentId));

        if (!salesItem.getWriter().equals(requestDto.getWriter()) ||
                !salesItem.getPassword().equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("아이템 작성자만이 댓글에 답변을 달 수 있습니다.");
        }

        comment.registerReply(requestDto.getReply());
        commentRepository.save(comment);
    }

    // 답글 등록 (유저 O -> V2)
    @Transactional
    public void registerReply(Long itemId, Long commentId, CommentRequestDto requestDto, String username) {
        SalesItem salesItem = salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 없습니다. id=" + itemId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + commentId));

        if (!salesItem.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("아이템 작성자만이 댓글에 답변을 달 수 있습니다.");
        }

        comment.registerReply(requestDto.getReply());
        commentRepository.save(comment);
    }

    // 답글 삭제 (유저 X -> V1)
    @Transactional
    public void deleteComment(Long itemId, Long commentId, CommentRequestDto requestDto) {
        commentRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + commentId));

        if (!comment.getWriter().equals(requestDto.getWriter()) || !comment.getPassword().equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("작성자 혹은 비밀번호가 일치하지 않습니다.");
        }

        commentRepository.delete(comment);
    }

    // 답글 삭제 (유저 O -> V2)
    @Transactional
    public void deleteComment(Long itemId, Long commentId, String username) {
        commentRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + commentId));

        if (!comment.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("댓글 작성자만 댓글을 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }
}
