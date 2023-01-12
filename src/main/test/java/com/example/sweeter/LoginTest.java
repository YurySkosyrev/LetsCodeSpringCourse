package com.example.sweeter;

import com.example.sweeter.controller.MessageController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Интеграционные тесты
 * @AutoConfigureMockMvc - Spring подменяет слой МVC фейковым слоем
 */


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageController messageController;

    // Через подмененный web-слой делаем get-запрос на главную страницу и проверяем ответ
    @Test
    public void contextLoads() throws Exception {
        this.mockMvc.perform(get("/")) // хотим выполнить get-запрос на главную страницу
                .andDo(print()) // выводим ошибки в консоль
                .andExpect(status().isOk()) // обертка метода Assert, ожидаем что статус get-запроса будет 200
                .andExpect(content().string(containsString("Hello, guest")))
                .andExpect(content().string(containsString("Please login"))); // ожидаем что вернёт контент, который будет содержать строку
    }

    // Проверка авторизации на странице
    @Test
    public void accessDeniedTest() throws Exception {
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(status().is3xxRedirection()) // проверяем, что вернет не ответ 200, но для простоты ожидаем ответ 300
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/messages-list-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void correctLoginTest() throws Exception {
        this.mockMvc.perform(formLogin().user("admin").password("123")) // смотрит как в контексте определена Login page и обращается к ней
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void badCredentialTest() throws Exception {
        this.mockMvc.perform(post("/login").param("user", "Petr"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


}
