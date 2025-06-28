package com.crewManager.pro.user.controller;

import com.crewManager.pro.user.domain.User;
import com.crewManager.pro.user.dto.response.UserResponse;
import com.crewManager.pro.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;



    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String  userId = authentication.getName(); // Long 타입 ID 사용
        User user = userService.findById(userId);

        // 기존 DTO 변환 메서드를 그대로 사용
        return ResponseEntity.ok(UserResponse.fromEntity(user));
    }
}
