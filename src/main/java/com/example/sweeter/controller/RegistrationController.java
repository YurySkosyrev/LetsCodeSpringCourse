package com.example.sweeter.controller;

import com.example.sweeter.domain.User;
import com.example.sweeter.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * Дополнительный контроллер для регистрации пользователя
 * По get-запросу отображается view с формой регистрации.
 * Из формы "registration" приходит post-запрос с данными нового юзера
 * если пользователь есть, то в модели передаётся переменная message на форму регистрации,
 * на которой в случае message!=null отображается сообщение
 * Если пользователя с такими данными нет в базе, то он сохраняется в БД.
 * Работа с БД проходит через Autowired объекта UserRepo
 */

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model){

        if(!userService.addUser(user)) {
            model.put("message", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if(isActivated) {
            model.addAttribute("message", "User succesfully activated");
        } else {
            model.addAttribute("message", "Activation code is not found!");
        }

        return "login";
    }
}
