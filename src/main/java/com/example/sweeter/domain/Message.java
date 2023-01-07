package com.example.sweeter.domain;

import com.example.sweeter.domain.util.MessageHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
@ToString(of = {"id", "text", "tag"})
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //добавляем валидацию на пустое сообщение и максимальную длину
    @NotBlank(message="Please fill the message")
    @Length(max = 2048, message = "Message too long. More then 2Kb")
    private String text;
    @Length(max = 255, message = "Message too long. More then 255")
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private String filename;

    @ManyToMany
    @JoinTable(
            name = "message_likes",
            joinColumns = { @JoinColumn(name = "message_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id")}
    )
    private Set<User> likes = new HashSet<>();

    public String getAuthorName() {
        return MessageHelper.getAuthorName(author);
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
