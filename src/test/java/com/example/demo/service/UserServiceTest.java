package com.example.demo.service;

import com.example.demo.config.JwtUtil;
import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository; // MemoryUserRepository 대신 인터페이스 사용
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


/**
 * Mockito를 사용하여 UserService의 단위 테스트를 진행합니다.
 */
@ExtendWith(MockitoExtension.class) // JUnit5에서 Mockito를 사용하겠다는 선언
class UserServiceTest {

    /**
     * @InjectMocks: 테스트 대상이 되는 클래스입니다.
     * 아래 @Mock으로 선언된 가짜 객체들이 자동으로 이 클래스에 주입됩니다.
     */
    @InjectMocks
    private UserService userService;

    /**
     * @Mock: 가짜로 만들어낼 객체들입니다.
     * 실제 동작은 하지 않고, 우리가 정의하는 행동만 흉내 냅니다.
     */
    @Mock
    private UserRepository userRepository; // 이제 MemoryUserRepository가 아닌 인터페이스를 사용합니다.

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;


    @Test
    void 회원가입_성공_테스트() {
        // given (이런 데이터가 주어졌을 때)
        User user = new User();
        user.setId(1L); // 테스트를 위해 ID를 임의로 설정
        user.setUsername("spring");
        user.setPassword("1234");
        user.setNickname("스프링");

        // Mock 객체들의 행동을 정의합니다. (가장 중요한 부분!)
        // 1. "어떤 username으로든 중복 체크를 시도하면, 결과는 비어있다(empty)고 해줘"
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        // 2. "어떤 문자열이든 암호화를 시도하면, 'encodedPassword'라고 반환해줘"
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        // 3. "어떤 User 객체든 저장하려고 하면, 그냥 그 User 객체를 그대로 돌려줘"
        when(userRepository.save(any(User.class))).thenReturn(user);


        // when (회원가입 메서드를 실행하면)
        Long savedId = userService.join(user);


        // then (결과는 이래야 한다)
        assertThat(savedId).isEqualTo(1L);
        assertThat(user.getPassword()).isEqualTo("encodedPassword"); // 암호화된 비밀번호로 변경되었는지 확인
    }

    @Test
    void 중복_회원_예외_테스트() {
        // given
        User existingUser = new User();
        existingUser.setUsername("spring");

        User newUser = new User();
        newUser.setUsername("spring");

        // "spring"이라는 이름으로 유저를 찾으려고 하면, 이미 존재하는 'existingUser'를 반환하라고 설정
        when(userRepository.findByUsername("spring")).thenReturn(Optional.of(existingUser));


        // when & then (이것을 실행하면 예외가 발생해야 한다)
        // newUser로 회원가입을 시도하면, IllegalStateException이 발생하는 것이 성공!
        assertThrows(IllegalStateException.class, () -> {
            userService.join(newUser);
        });
    }
}