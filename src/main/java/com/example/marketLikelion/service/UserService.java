package com.example.marketLikelion.service;

import com.example.marketLikelion.dto.response.UserResponseDto;
import com.example.marketLikelion.entity.User;
import com.example.marketLikelion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다." + username));
        return UserResponseDto.of(user);
    }

}
