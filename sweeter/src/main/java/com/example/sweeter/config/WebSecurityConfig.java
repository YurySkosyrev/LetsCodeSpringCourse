package com.example.sweeter.config;

import com.example.sweeter.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 * Ниже приведена настройка безопасности, которая гарантирует,
 * что только авторизованные пользователи смогут перейти на главную страницу
 *
 * -> @EnableWebMvcSecurity - включение поддержки безопасности Spring Security и Spring MVC интеграции.
 *
 * Метод configure(HttpSecurity) определяет, какие URL пути должны быть защищены, а какие нет.
 * "/" и "/registration" настроены без требования к авторизации (метод permitAll())
 * Ко всем остальным путям должна быть произведена аутентификация (authenticated())
 *
 * В методе configure(AuthenticationManagerBuilder auth) из БД достаём юзеров, пароли и их роли
 * dataSource нужен для того чтобы ходить в БД
 * passwordEncoder задаёт способ шифрования паролей
 * usersByUsernameQuery нужен для того чтобы система могла найти пользователя по его имени.
 * Запрос именно в таком порядке!!!
 * authoritiesByUsernameQuery - помогает Spring получить список пользователей с их ролями
 *
 * -> @EnableGlobalMethodSecurity(prePostEnabled = true) - нужно чтобы в UserController заработала
 * проверка прав доступа у юзера перед выполнением методов
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    Больше не нужно получаем пользователей с помощью UserService
//    private final DataSource dataSource;
    private final UserService userService;

    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                        .antMatchers("/", "/registration").permitAll()
                        .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());

//                Вместо запросов получаем пользователей с помощью UserService
//
//                auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .passwordEncoder(NoOpPasswordEncoder.getInstance())
//                .usersByUsernameQuery("select username, password, active from usr where username=?")
//                .authoritiesByUsernameQuery("select u.username, ur.roles from usr u inner join user_role ur on " +
//                        "u.id = ur.user_id where u.username=?");
    }
}