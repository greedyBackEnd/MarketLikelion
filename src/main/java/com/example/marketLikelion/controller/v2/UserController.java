package com.example.marketLikelion.controller.v2;

import com.example.marketLikelion.dto.response.UserResponseDto;
import com.example.marketLikelion.entity.CustomUserDetails;
import com.example.marketLikelion.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    private ResponseEntity<UserResponseDto> myProfile(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info(userDetails.getUsername());
        UserResponseDto userProfile = userService.getUserProfile(userDetails.getUsername());
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }
}
