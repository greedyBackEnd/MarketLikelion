package com.example.marketLikelion.controller.v2;

import com.example.marketLikelion.dto.request.SalesItemRequestDto;
import com.example.marketLikelion.dto.response.SalesItemResponseDto;
import com.example.marketLikelion.jwt.JwtTokenUtils;
import com.example.marketLikelion.service.SalesItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/items")
public class SalesItemControllerV2 {

    private final SalesItemService salesItemService;
    private final JwtTokenUtils jwtTokenUtils;

    // 상품 등록
    @PostMapping
    public ResponseEntity<Map<String, String>> registerItem(@RequestBody SalesItemRequestDto requestDto,
                                                            @RequestPart(value = "file", required = false) Optional<MultipartFile> fileOptional) throws IOException {
        String username = jwtTokenUtils.getCurrentUsername();
        MultipartFile file = fileOptional.orElse(null);
        salesItemService.registerItem(requestDto, username, file);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "등록이 완료되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    // 상품 전체 조회
    @GetMapping
    public ResponseEntity<Page<SalesItemResponseDto>> getItems(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<SalesItemResponseDto> items = salesItemService.getItems(pageable);

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    // 상품 단일 조회
    @GetMapping("/{itemId}")
    public ResponseEntity<SalesItemResponseDto> getItem(@PathVariable Long itemId) {
        SalesItemResponseDto item = salesItemService.getOneItem(itemId);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    // 상품 수정
    @PutMapping("/{itemId}")
    public ResponseEntity<Map<String, String>> updateItem(@PathVariable Long itemId,
                                                          @RequestBody SalesItemRequestDto requestDto) {
        String username = jwtTokenUtils.getCurrentUsername();
        salesItemService.updateItem(itemId, requestDto, username);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "물품 정보가 수정되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    // 상품 이미지 수정 및 업로드
    @PutMapping("/{itemId}/image")
    public ResponseEntity<Map<String, String>> updateItemImage(@PathVariable Long itemId,
                                                               @RequestParam("image") MultipartFile imageFile) {
        String username = jwtTokenUtils.getCurrentUsername();
        salesItemService.updateItemImage(itemId, imageFile, username);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "이미지가 등록되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    // 상품 삭제
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Map<String, String>> deleteItem(@PathVariable Long itemId) {
        String username = jwtTokenUtils.getCurrentUsername();
        salesItemService.deleteItem(itemId, username);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "물품을 삭제했습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
