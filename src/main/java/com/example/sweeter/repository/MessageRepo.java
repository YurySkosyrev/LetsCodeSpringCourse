package com.example.sweeter.repository;

import com.example.sweeter.domain.Message;
import com.example.sweeter.domain.User;
import com.example.sweeter.domain.dto.MessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Интерфейс для работы с БД для сущности Message
 * Название метода записывается в соответствии с шаблонами Spring
 */

public interface MessageRepo extends CrudRepository<Message, Long> {

    @Query("select new com.example.sweeter.domain.dto.MessageDto( " +
            " m, " +
            "count(ml), " +
            "sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "group by m")
    Page<MessageDto> findAll(Pageable pageable, @Param("user") User user);

    @Query("select new com.example.sweeter.domain.dto.MessageDto( " +
            "m, " +
            "count(ml), " +
            "sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "where m.tag = :tag " +
            "group by m")
    Page<MessageDto> findByTag(@Param("tag") String tag, Pageable pageable, @Param("user") User user);

    // Запрос на языке HQL, синтаксис схож с SQL, но более простой
    // Spring позволяет писать запросы сразу в методах репозитория
    // Pageable добавляется автоматически к запросу
    // Не требуется полное описание запроса
    // Plaseholder добавляется с помощью аннотации @Param
    @Query("select new com.example.sweeter.domain.dto.MessageDto(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "where m.author = :author " +
            "group by m")
    Page<MessageDto> findByUser(Pageable pageable, @Param("author") User author, @Param("user") User user);
}
