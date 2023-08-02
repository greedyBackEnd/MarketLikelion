package com.example.marketLikelion.service;

import com.example.marketLikelion.dto.request.UserUpdateDto;
import com.example.marketLikelion.dto.response.UserResponseDto;
import com.example.marketLikelion.entity.User;
import com.example.marketLikelion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 유저 정보 조회
    public UserResponseDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다." + username));
        return UserResponseDto.of(user);
    }

    // 유저 정보 수정
    @Transactional
    public void updateUser(String username, UserUpdateDto updateDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));
        user.updateUser(updateDto.toEntity());
        userRepository.save(user);
    }

}
