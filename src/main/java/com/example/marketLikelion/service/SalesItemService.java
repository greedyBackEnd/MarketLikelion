package com.example.marketLikelion.service;

import com.example.marketLikelion.dto.request.SalesItemRequestDto;
import com.example.marketLikelion.dto.response.SalesItemResponseDto;
import com.example.marketLikelion.entity.SalesItem;
import com.example.marketLikelion.entity.enumStatus.ItemStatus;
import com.example.marketLikelion.repository.SalesItemRepository;
import com.example.marketLikelion.repository.UserRepository;
import com.example.marketLikelion.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SalesItemService {

    private final SalesItemRepository salesItemRepository;
    private final UserRepository userRepository;

    // 아이템 등록 (유저 X -> V1)
    @Transactional
    public void registerItem(SalesItemRequestDto requestDto, MultipartFile file) throws IOException {
        String filename = "";
        if (file != null && !file.isEmpty()) {
            filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String uploadDir = "upload/";

            FileUploadUtil.saveFile(uploadDir, filename, file);
        } else {
            filename = "/static/images/defaultImage.png";
        }

        SalesItem entity = requestDto.toEntity().toBuilder()
                .imageUrl(filename)
                .status(ItemStatus.ON_SALE.getStatus())
                .build();
        salesItemRepository.save(entity);
    }

    // 아이템 등록 (유저 O -> V2)
    @Transactional
    public void registerItem(SalesItemRequestDto requestDto, String username, MultipartFile file) throws IOException {
        String filename;
        if (file != null && !file.isEmpty()) {
            filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String uploadDir = "upload/";

            FileUploadUtil.saveFile(uploadDir, filename, file);
        } else {
            filename = "/static/images/defaultImage.png";
        }

        SalesItem entity = requestDto.toEntity().toBuilder()
                .imageUrl(filename)
                .status(ItemStatus.ON_SALE.getStatus())
                .user(userRepository.findByUsername(username).get())
                .build();
        salesItemRepository.save(entity);
    }

    // 아이템 전체 조회
    public Page<SalesItemResponseDto> getItems(Pageable pageable) {
        return salesItemRepository.findAll(pageable)
                .map(SalesItemResponseDto::of);
    }

    // 아이템 단일 조회
    public SalesItemResponseDto getOneItem(Long itemId) {
        SalesItem salesItem = salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));
        return SalesItemResponseDto.of(salesItem);
    }

    // 아이템 정보 수정 (유저 X -> V1)
    @Transactional
    public void updateItem(Long itemId, SalesItemRequestDto requestDto) {
        SalesItem salesItem = salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));

        if (!salesItem.getPassword().equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        salesItem.update(requestDto.toEntity());
    }

    // 아이템 정보 수정 (유저 O -> V2)
    @Transactional
    public void updateItem(Long itemId, SalesItemRequestDto requestDto, String username) {
        SalesItem salesItem = salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));

        if (!salesItem.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("다른 유저가 작성한 글입니다.");
        }

        salesItem.update(requestDto.toEntity());
    }

    // 아이템 이미지 업데이트 (유저 X -> V1)
    @Transactional
    public void updateItemImage(Long itemId, MultipartFile imageFile, String writer, String password) {
        SalesItem salesItem = salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));

        if (!salesItem.getWriter().equals(writer) || !salesItem.getPassword().equals(password)) {
            throw new IllegalArgumentException("작성자 혹은 비밀번호가 일치하지 않습니다.");
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));
        String uploadDir = "static/images";

        try {
            FileUploadUtil.saveFile(uploadDir, fileName, imageFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("이미지 파일 업로드에 실패하였습니다.");
        }

        String imageUrl = "/" + uploadDir + "/" + fileName;
        salesItem.updateImage(imageUrl);
    }


    // 아이템 이미지 업데이트 (유저 O -> V2)
    @Transactional
    public void updateItemImage(Long itemId, MultipartFile imageFile, String username) {
        SalesItem salesItem = salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));

        if (!salesItem.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("다른 유저가 작성한 글입니다.");
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));
        String uploadDir = "static/images";

        try {
            FileUploadUtil.saveFile(uploadDir, fileName, imageFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("이미지 파일 업로드에 실패하였습니다.");
        }

        String imageUrl = "/" + uploadDir + "/" + fileName;
        salesItem.updateImage(imageUrl);
    }

    // 아이템 삭제 (유저 X -> V1)
    @Transactional
    public void deleteItem(Long itemId, SalesItemRequestDto requestDto) {
        SalesItem salesItem = salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));

        if (!salesItem.getWriter().equals(requestDto.getWriter()) || !salesItem.getPassword().equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("작성자 혹은 비밀번호가 일치하지 않습니다.");
        }

        salesItemRepository.delete(salesItem);
    }

    // 아이템 삭제 (유저 O -> V2)
    @Transactional
    public void deleteItem(Long itemId, String username) {
        SalesItem salesItem = salesItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + itemId));

        if (!salesItem.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("다른 유저가 작성한 글입니다.");
        }
        salesItemRepository.delete(salesItem);
    }
}
