package service;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import repository.JpaUserRepository;
import repository.UserRepository;

@Configuration
public class SpringConfig {

    private final EntityManager em;

    // 스프링이 EntityManager를 주입해줌
    @Autowired
    public SpringConfig(EntityManager em) {
        this.em = em;
    }

    // UserRepository를 빈으로 등록
    @Bean
    public UserRepository userRepository() {
        // 우리가 직접 만든 JpaUserRepository를 생성해서 등록
        // 이때 EntityManager를 넘겨줌
        return new JpaUserRepository(em);
    }
}
