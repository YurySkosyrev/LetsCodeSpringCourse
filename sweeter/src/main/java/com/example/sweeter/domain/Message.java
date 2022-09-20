package com.example.sweeter.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Сущность Message, для хранения сообщений в БД
 *
 * -> @Entity - показывает, что это не просто код, а сущность
 * -> @GeneratedValue(strategy = GenerationType.AUTO) - id формируется БД автоматически
 *
 * Поля ищутся в таблице по их названию, т.к. нет аннотации @Column
 * Так же и с названием таблицы - названию класса, т.к. нет аннотации @Table
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String text;
    private String tag;

    private User author;

    public Message(String text, String tag) {
        this.text = text;
        this.tag = tag;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
