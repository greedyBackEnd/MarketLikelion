package com.example.marketLikelion.controller.v2;

import com.example.marketLikelion.dto.request.NegotiationRequestDto;
import com.example.marketLikelion.dto.response.NegotiationResponseDto;
import com.example.marketLikelion.jwt.JwtTokenUtils;
import com.example.marketLikelion.service.NegotiationService;
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
@RequestMapping("/api/v2/items/{itemId}/proposals")
public class NegotiationControllerV2 {

    private final NegotiationService negotiationService;
    private final JwtTokenUtils jwtTokenUtils;

    @PostMapping
    public ResponseEntity<Map<String, String>> registerNegotiation(@PathVariable Long itemId,
                                                                   @RequestBody NegotiationRequestDto requestDto) {
        String username = jwtTokenUtils.getCurrentUsername();
        negotiationService.registerNegotiation(itemId, requestDto, username);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "구매 제안이 등록되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<NegotiationResponseDto>> getNegotiations(@PathVariable Long itemId,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int limit) {
        String username = jwtTokenUtils.getCurrentUsername();
        Pageable pageable = PageRequest.of(page, limit);
        Page<NegotiationResponseDto> negotiations = negotiationService.getNegotiations(itemId, username, pageable);

        return new ResponseEntity<>(negotiations, HttpStatus.OK);
    }

    @PutMapping("/{proposalId}")
    public ResponseEntity<Map<String, String>> changeNegotiationStatus(@PathVariable Long itemId,
                                                                       @PathVariable Long proposalId,
                                                                       @RequestBody NegotiationRequestDto requestDto) {
        String username = jwtTokenUtils.getCurrentUsername();

        if (requestDto.getStatus() != null) {
            negotiationService.changeNegotiationStatus(itemId, proposalId, requestDto, username);
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "제안의 상태가 변경되었습니다.");
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }

        if (requestDto.getSuggestedPrice() != null) {
            negotiationService.updateNegotiation(itemId, proposalId, requestDto, username);
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "제안이 수정되었습니다.");
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }

        throw new IllegalArgumentException("잘못된 요청입니다.");
    }

    @DeleteMapping("/{proposalId}")
    public ResponseEntity<Map<String, String>> deleteNegotiation(@PathVariable Long itemId,
                                                                 @PathVariable Long proposalId) {
        String username = jwtTokenUtils.getCurrentUsername();
        negotiationService.deleteNegotiation(itemId, proposalId, username);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "제안을 삭제했습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
