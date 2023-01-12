package com.example.sweeter.config;

import com.example.sweeter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Ниже приведена настройка безопасности, которая гарантирует,
 * что только авторизованные пользователи смогут перейти на главную страницу
 *
 * -> @EnableWebSecurity - включение поддержки безопасности Spring Security и Spring MVC интеграции.
 *
 * -> @EnableGlobalMethodSecurity(prePostEnabled = true) - нужно чтобы в UserController заработала
 * проверка прав доступа у юзера перед выполнением методов
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(UserService userService,
                             PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Метод определяет, какие URL пути должны быть защищены, а какие нет.
     * "/" и "/registration" настроены без требования к авторизации (метод permitAll())
     * Ко всем остальным путям должна быть произведена аутентификация (authenticated())*
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                        .antMatchers("/", "/registration", "/static/**", "/activate/*").permitAll()
                        .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .rememberMe()
                .and()
                .logout()
                .permitAll();
    }


    /**
     * Метод получения юзеров с паролями и ролями
     */

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);

        /**
         * Другой вариант извлечения пользователей
         */
//        /**
//         * dataSource нужен для того чтобы ходить в БД
//         * passwordEncoder задаёт способ шифрования паролей
//         * usersByUsernameQuery нужен для того чтобы система могла найти пользователя по его имени.
//         * Запрос именно в таком порядке!!!
//         * authoritiesByUsernameQuery - помогает Spring получить список пользователей с их ролями
//         */
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