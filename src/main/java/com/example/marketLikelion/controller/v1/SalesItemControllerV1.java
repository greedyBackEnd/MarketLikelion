package com.example.marketLikelion.controller.v1;

import com.example.marketLikelion.dto.request.SalesItemRequestDto;
import com.example.marketLikelion.dto.response.SalesItemResponseDto;
import com.example.marketLikelion.service.SalesItemService;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class SalesItemControllerV1 {

    private final SalesItemService salesItemService;

    @PostMapping
    public ResponseEntity<Map<String, String>> registerItem(@RequestBody SalesItemRequestDto requestDto,
                                                            @RequestPart(value = "file", required = false) Optional<MultipartFile> fileOptional) throws IOException {
        MultipartFile file = fileOptional.orElse(null);
        salesItemService.registerItem(requestDto, file);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "등록이 완료되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<SalesItemResponseDto>> getItems(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<SalesItemResponseDto> items = salesItemService.getItems(pageable);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<SalesItemResponseDto> getItem(@PathVariable Long itemId) {
        SalesItemResponseDto item = salesItemService.getOneItem(itemId);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<Map<String, String>> updateItem(@PathVariable Long itemId,
                                                          @RequestBody SalesItemRequestDto requestDto) {
        salesItemService.updateItem(itemId, requestDto);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "물품 정보가 수정되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PutMapping("/{itemId}/image")
    public ResponseEntity<Map<String, String>> updateItemImage(@PathVariable Long itemId,
                                                               @RequestParam("image") MultipartFile imageFile,
                                                               @RequestParam("writer") String writer,
                                                               @RequestParam("password") String password) {
        salesItemService.updateItemImage(itemId, imageFile, writer, password);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "이미지가 등록되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Map<String, String>> deleteItem(@PathVariable Long itemId,
                                                          @RequestBody SalesItemRequestDto requestDto) {
        salesItemService.deleteItem(itemId, requestDto);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "물품을 삭제했습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
