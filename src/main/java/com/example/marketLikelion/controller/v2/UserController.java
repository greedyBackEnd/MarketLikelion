package com.example.marketLikelion.controller.v2;

import com.example.marketLikelion.dto.request.UserUpdateDto;
import com.example.marketLikelion.dto.response.UserResponseDto;
import com.example.marketLikelion.entity.CustomUserDetails;
import com.example.marketLikelion.jwt.JwtTokenUtils;
import com.example.marketLikelion.service.JpaUserDetailsManager;
import com.example.marketLikelion.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final JpaUserDetailsManager userDetailsManager;

    @GetMapping("/profile")
    private ResponseEntity<UserResponseDto> myProfile(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info(userDetails.getUsername());
        UserResponseDto userProfile = userService.getUserProfile(userDetails.getUsername());
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    @PutMapping("/profile")
    private ResponseEntity<Map<String, String>> updateProfile(@RequestBody UserUpdateDto userUpdateDto) {
        String username = jwtTokenUtils.getCurrentUsername();
        userService.updateUser(username, userUpdateDto);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "유저 정보가 수정되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
