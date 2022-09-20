package com.example.sweeter.controller;

import com.example.sweeter.domain.Role;
import com.example.sweeter.domain.User;
import com.example.sweeter.repository.UserRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

/**
 * Дополнительный контроллер для регистрации пользователя
 * По get-запросу отображается view с формой регистрации.
 * Из формы "registration" приходит post-запрос с данными нового юзера
 * если пользователь есть, то в модели передаётся переменная message на форму регистрации,
 * на которой в случае massage!=null отображается сообщение
 * Если пользователя с такими данными нет в базе, то он сохраняется в БД.
 * Работа с БД проходит через Autowired объекта UserRepo
 */

@Controller
public class RegistrationController {

    private final UserRepo userRepo;

    public RegistrationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model){
        User userFromDB = userRepo.findByUsername(user.getUsername());

        if(userFromDB != null) {
            model.put("message", "User exists!");
            return "registration";
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

        return "redirect:/login";
    }
}
