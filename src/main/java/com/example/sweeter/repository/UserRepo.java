package com.example.sweeter.repository;

import com.example.sweeter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс для работы с БД для сущности User
 * Название метода записывается в соответствии с шаблонами Spring
 */

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByActivationCode(String code);
}
