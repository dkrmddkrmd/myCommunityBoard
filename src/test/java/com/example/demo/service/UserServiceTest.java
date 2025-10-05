package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.repository.MemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    UserService userService;
    MemoryUserRepository memberRepository;

    @BeforeEach // 각 테스트를 실행하기 전에 먼저 실행되는 메서드
    public void beforeEach() {
        // 테스트마다 독립적인 리포지토리와 서비스를 새로 생성
        memberRepository = new MemoryUserRepository();
        userService = new UserService(memberRepository);
    }

    @Test
    void 회원가입_성공_테스트() {
        // given (이런 데이터가 주어졌을 때)
        User user = new User();
        user.setUsername("spring");
        user.setPassword("1234");
        user.setNickname("스프링");

        // when (이것을 실행하면)
        Long savedId = userService.join(user);

        // then (결과는 이래야 한다)
        User foundUser = memberRepository.findById(savedId).get();
        assertThat(user.getUsername()).isEqualTo(foundUser.getUsername());
    }

    @Test
    void 중복_회원_예외_테스트() {
        // given
        User user1 = new User();
        user1.setUsername("spring");

        User user2 = new User();
        user2.setUsername("spring");

        // when
        userService.join(user1);

        // then
        // userService.join(user2)를 실행하면 IllegalStateException이 발생하는 것이 성공!
        assertThrows(IllegalStateException.class, () -> {
            userService.join(user2);

        });
    }
}