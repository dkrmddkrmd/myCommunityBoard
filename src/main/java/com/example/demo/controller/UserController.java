package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.UserForm;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "사용자 API", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "회원 가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "회원가입 성공")
    @ApiResponse(responseCode = "409", description = "이미 존재하는 아이디")
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserForm form) {
        User user = new User();
        user.setUsername(form.getUsername());
        user.setPassword(form.getPassword());
        user.setNickname(form.getNickname());

        try {
            Long savedUserId = userService.join(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUserId);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Operation(summary = "로그인", description = "사용자 이름과 비밀번호로 로그인하고 JWT 토큰을 발급받습니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공 및 토큰 발급")
    @ApiResponse(responseCode = "401", description = "인증 실패")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto) {
        String token = userService.login(requestDto.getUsername(), requestDto.getPassword());
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    @Operation(summary = "사용자 정보 조회", description = "ID로 특정 사용자의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserInfo(
            @Parameter(description = "사용자 ID", required = true) @PathVariable Long id
    ) {
        Optional<User> userOptional = userService.findUser(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserResponseDto responseDto = new UserResponseDto(user.getId(), user.getUsername(), user.getNickname());
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}