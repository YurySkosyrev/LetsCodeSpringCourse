package com.example.sweeter.repository;

import com.example.sweeter.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Интерфейс для работы с БД для сущности Message
 * Название метода записывается в соответствии с шаблонами Spring
 */

public interface MessageRepo extends CrudRepository<Message, Long> {

    Page<Message> findByTag(String tag, Pageable pageable);

    Page<Message> findAll(Pageable pageable);
}
