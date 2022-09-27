package com.example.sweeter.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Сущность Message, для хранения сообщений в БД
 *
 * -> @Entity - показывает, что это не просто код, а сущность
 * -> @GeneratedValue(strategy = GenerationType.AUTO) - id формируется БД автоматически
 *
 * Поля ищутся в таблице по их названию, т.к. нет аннотации @Column
 * Так же и с названием таблицы - названию класса, т.к. нет аннотации @Table
 *
 * Поле author маппится аннотацией @ManyToOne, чтобы установить связь с таблицей user.
 * С помощью аннотации @JoinColumn укажем чтобы в таблице massage поле,
 * по которому осуществляется соединение называлось "user_id", а не author_id как по умолчанию
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private String filename;

    public String getAuthorName(){
        return author != null ? author.getUsername() : "<none>";
    }

    public Message(String text, String tag, User user) {
        this.text = text;
        this.tag = tag;
        this.author = user;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
