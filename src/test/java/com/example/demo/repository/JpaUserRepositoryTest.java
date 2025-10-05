package com.example.demo.repository;

import com.example.demo.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional // 테스트가 끝나면 데이터를 롤백하여 DB를 깨끗하게 유지
class JpaUserRepositoryTest {

    @Autowired // 스프링 컨테이너에서 실제 JpaUserRepository 빈을 주입받음
    UserRepository userRepository;

    @Test
    @Commit
    void 저장_및_username으로_조회_테스트() {
        // given
        User user = new User();
        user.setUsername("testuser");
        user.setNickname("테스터");

        // when
        userRepository.save(user);
        User foundUser = userRepository.findByUsername("testuser").get();

        // then
        assertThat(foundUser.getId()).isEqualTo(user.getId());
        assertThat(foundUser.getUsername()).isEqualTo(user.getUsername());
    }
}