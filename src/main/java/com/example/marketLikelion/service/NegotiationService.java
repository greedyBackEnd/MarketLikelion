package com.example.marketLikelion.service;

import com.example.marketLikelion.dto.request.NegotiationRequestDto;
import com.example.marketLikelion.dto.response.NegotiationResponseDto;
import com.example.marketLikelion.entity.Negotiation;
import com.example.marketLikelion.entity.SalesItem;
import com.example.marketLikelion.entity.User;
import com.example.marketLikelion.entity.enumStatus.NegotiationStatus;
import com.example.marketLikelion.repository.NegotiationRepository;
import com.example.marketLikelion.repository.SalesItemRepository;
import com.example.marketLikelion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NegotiationService {

    private final SalesItemRepository salesItemRepository;
    private final NegotiationRepository negotiationRepository;
    private final UserRepository userRepository;

    // 제안 등록 (유저 X -> V1)
    @Transactional
    public void registerNegotiation(Long itemId, NegotiationRequestDto requestDto) {
        SalesItem salesItem = salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));
        Negotiation negotiation = requestDto.toEntity().toBuilder()
                .salesItem(salesItem)
                .status(NegotiationStatus.SUGGESTED.getStatus())
                .build();
        negotiationRepository.save(negotiation);
    }

    // 제안 등록 (유저 O -> V2)
    @Transactional
    public void registerNegotiation(Long itemId, NegotiationRequestDto requestDto, String username) {
        SalesItem salesItem = salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));

        Negotiation negotiation = requestDto.toEntity().toBuilder()
                .salesItem(salesItem)
                .status(NegotiationStatus.SUGGESTED.getStatus())
                .user(userRepository.findByUsername(username).get())
                .build();
        negotiationRepository.save(negotiation);
    }

    // 제안 조회 (유저 X -> V1)
    public Page<NegotiationResponseDto> getNegotiations(Long itemId, String writer, String password, Pageable pageable) {
        SalesItem salesItem = salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));

        if (salesItem.getWriter().equals(writer) && salesItem.getPassword().equals(password)) {
            return negotiationRepository.findBySalesItem(salesItem, pageable)
                    .map(NegotiationResponseDto::of);
        } else {
            return negotiationRepository.findBySalesItemAndWriterAndPassword(salesItem, writer, password, pageable)
                    .map(NegotiationResponseDto::of);
        }
    }

    // 제안 조회 (유저 O -> V2)
    public Page<NegotiationResponseDto> getNegotiations(Long itemId, String username, Pageable pageable) {
        SalesItem salesItem = salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));
        User user = userRepository.findByUsername(username).get();

        if (salesItem.getUser().getUsername().equals(username)) {
            return negotiationRepository.findBySalesItem(salesItem, pageable)
                    .map(NegotiationResponseDto::of);
        } else {
            return negotiationRepository.findBySalesItemAndUser(salesItem, user, pageable)
                    .map(NegotiationResponseDto::of);
        }
    }

    // 제안 수정 (유저 X -> V1)
    @Transactional
    public void updateNegotiation(Long itemId, Long proposalId, NegotiationRequestDto requestDto) {
        salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));

        Negotiation negotiation = negotiationRepository.findById(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("제안이 존재하지 않습니다. id=" + proposalId));

        if (!negotiation.getWriter().equals(requestDto.getWriter()) || !negotiation.getPassword().equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("작성자나 비밀번호가 일치하지 않습니다.");
        }

        negotiation.updateSuggestedPrice(requestDto.getSuggestedPrice());
        negotiationRepository.save(negotiation);
    }

    // 제안 수정 (유저 O -> V2)
    @Transactional
    public void updateNegotiation(Long itemId, Long proposalId, NegotiationRequestDto requestDto, String username) {
        salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));

        Negotiation negotiation = negotiationRepository.findById(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("제안이 존재하지 않습니다. id=" + proposalId));

        if (!negotiation.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("다른 유저의 제안입니다.");
        }

        negotiation.updateSuggestedPrice(requestDto.getSuggestedPrice());
        negotiationRepository.save(negotiation);
    }

    // 제안 삭제 (유저 X -> V1)
    @Transactional
    public void deleteNegotiation(Long itemId, Long proposalId, NegotiationRequestDto requestDto) {
        salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));

        Negotiation negotiation = negotiationRepository.findById(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("해당 제안이 존재하지 않습니다. id=" + proposalId));

        if (!negotiation.getWriter().equals(requestDto.getWriter()) || !negotiation.getPassword().equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("작성자 또는 비밀번호가 일치하지 않습니다.");
        }

        negotiationRepository.delete(negotiation);
    }

    // 제안 삭제 (유저 O -> V2)
    @Transactional
    public void deleteNegotiation(Long itemId, Long proposalId, String username) {
        salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));

        Negotiation negotiation = negotiationRepository.findById(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("해당 제안이 존재하지 않습니다. id=" + proposalId));

        if (!negotiation.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("다른 유저의 제안입니다.");
        }

        negotiationRepository.delete(negotiation);
    }

    // 제안 수락 및 확정 (유저 X -> V1)
    @Transactional
    public void changeNegotiationStatus(Long itemId, Long proposalId, NegotiationRequestDto requestDto) {
        SalesItem salesItem = salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));

        Negotiation negotiation = negotiationRepository.findById(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("해당 제안이 존재하지 않습니다. id=" + proposalId));

        if (!salesItem.getWriter().equals(requestDto.getWriter()) || !salesItem.getPassword().equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("작성자 또는 비밀번호가 일치하지 않습니다.");
        }

        if (requestDto.getStatus().equals("확정") && !negotiation.getStatus().equals("수락")) {
            throw new IllegalArgumentException("제안이 수락 상태에서만 확정 가능합니다.");
        }

        negotiation.updateStatus(requestDto.getStatus());

        if (requestDto.getStatus().equals("확정")) {
            salesItem.updateStatus("판매 완료");
            List<Negotiation> otherNegotiations = negotiationRepository.findBySalesItemAndIdNot(salesItem, proposalId);
            for (Negotiation otherNegotiation : otherNegotiations) {
                otherNegotiation.updateStatus("거절");
            }
        }
    }

    // 제안 수락 및 확정 (유저 O -> V2)
    @Transactional
    public void changeNegotiationStatus(Long itemId, Long proposalId, NegotiationRequestDto requestDto, String username) {

        SalesItem salesItem = salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));

        Negotiation negotiation = negotiationRepository.findById(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("해당 제안이 존재하지 않습니다. id=" + proposalId));

        if (!salesItem.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("다른 유저가 작성한 글입니다.");
        }

        if (requestDto.getStatus().equals("확정") && !negotiation.getStatus().equals("수락")) {
            throw new IllegalArgumentException("제안이 수락 상태에서만 확정 가능합니다.");
        }

        negotiation.updateStatus(requestDto.getStatus());

        if (requestDto.getStatus().equals("확정")) {
            salesItem.updateStatus("판매 완료");
            List<Negotiation> otherNegotiations = negotiationRepository.findBySalesItemAndIdNot(salesItem, proposalId);
            for (Negotiation otherNegotiation : otherNegotiations) {
                otherNegotiation.updateStatus("거절");
            }
        }
    }
}
