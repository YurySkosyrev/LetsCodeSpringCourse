package com.example.sweeter.repository;

import com.example.sweeter.domain.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Интерфейс для работы с БД для сущности Message
 * Название метода записывается в соответствии с шаблонами Spring
 */

public interface MessageRepo extends CrudRepository<Message, Long> {

    List<Message> findByTag(String tag);

}
