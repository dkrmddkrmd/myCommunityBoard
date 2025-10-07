package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.UserForm;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users") // 이 컨트롤러의 API는 모두 /api/users로 시작
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원 가입 API
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserForm form) {
        User user = new User();
        user.setUsername(form.getUsername());
        user.setPassword(form.getPassword());
        user.setNickname(form.getNickname());

        try {
            Long savedUserId = userService.join(user);

            // 회원가입 성공: 201 Created 상태 코드와 함께 생성된 유저의 ID를 반환
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUserId);

        } catch (IllegalStateException e) {
            // 중복 아이디 예외 발생 시: 409 Conflict 상태 코드와 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto) {
        String token = userService.login(requestDto.getUsername(), requestDto.getPassword());

        if (token != null) {
            // 로그인 성공: 200 OK 상태 코드와 함께 토큰을 Body에 담아 반환
            return ResponseEntity.ok(token);
        } else {
            // 로그인 실패: 401 Unauthorized 상태 코드 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    // 내 정보 조회 API (일단은 ID를 직접 받아서 처리)
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserInfo(@PathVariable Long id) {
        Optional<User> userOptional = userService.findUser(id);

        if (userOptional.isPresent()) {
            // 사용자 정보 조회 성공
            User user = userOptional.get();
            UserResponseDto responseDto = new UserResponseDto(user.getId(), user.getUsername(), user.getNickname());
            return ResponseEntity.ok(responseDto);
        } else {
            // 사용자가 없을 경우 404 Not Found 반환
            return ResponseEntity.notFound().build();
        }
    }
}