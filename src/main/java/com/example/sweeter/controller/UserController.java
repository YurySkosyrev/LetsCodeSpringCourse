package com.example.sweeter.controller;

import com.example.sweeter.domain.Role;
import com.example.sweeter.domain.User;
import com.example.sweeter.repository.UserRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Контроллер для управления пользователями
 *
 * -> @RequestMapping("/user") - чтобы не подписыать у каждого метода дополнительный путь /user
 *
 * -> @PreAuthorize("hasAuthority('ADMIN')") - аннотация будет для каждого метода в данном контроллере проверять
 * перед выполнением метода наличие у пользователя прав, указанных в скобках
 * Так же в WebSecurityConfig необходимо добавить аннотацию @EnableGlobalMethodSecurity
 */

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    private final UserRepo userRepo;

    public UserController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public String userList(Model model){
        model.addAttribute("users", userRepo.findAll());
        return "userList";
    }


    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model){
        // благодаря Spring сразу получаем пользователя без обращения к БД
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

//    Чтобы сохранить пользователя нужно получить аргументы с сервера
//    По параметру userId из формы userEdit.ftlh будем получать пользователя из БД
//    Так же нам нужно получить список полей, который передаётся в этой форме
//    Количество полей всегда разное и в БД попадут только отмеченные в чекбоксе
//    Для этого сделаем ещё один RequestParam Map<String, String> form
//    Так же в форме передаётся имя пользователя, его можно менять и мы должны его сохранить


    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user){

//        Устанавливаем пользователю новое имя
        user.setUsername(username);

//        Получаем список ролей, чтобы проверить, что они установлены данному пользователю
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

//        Очищаем массив ролей перед обновлением
        user.getRoles().clear();

//        Теперь нужно проверить, что форма содержит роли для данного пользователя
//        и выбрать из всех полей формы именно роли
        for (String key : form.keySet()){
            if (roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(user);
        return "redirect:/user";
    }
}
