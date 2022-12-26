package com.example.sweeter.service;

import com.example.sweeter.domain.Role;
import com.example.sweeter.domain.User;
import com.example.sweeter.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис для работы с пользователями в БД
 */

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final MailSenderService mailSender;

    public UserService(UserRepo userRepo, MailSenderService mailSender) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public boolean addUser(User user){
        User userFromDB = userRepo.findByUsername(user.getUsername());

        if(userFromDB != null){
            return false;
        }

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());

        userRepo.save(user);

        sendMessage(user);

        return true;
    }

    private void sendMessage(User user) {
        if(!StringUtils.isEmpty(user.getEmail())){
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Sweater. Please, visit next link: " +
                            "http://localhost:8090/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if (user == null){
            return false;
        }

        user.setActivationCode(null);
        user.setActive(true);
        userRepo.save(user);

        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {

//    Чтобы сохранить пользователя нужно получить аргументы с сервера
//    По параметру userId из формы userEdit.ftlh будем получать пользователя из БД
//    Так же нам нужно получить список полей, который передаётся в этой форме
//    Количество полей всегда разное и в БД попадут только отмеченные в чекбоксе
//    Для этого сделаем ещё один RequestParam Map<String, String> form
//    Так же в форме передаётся имя пользователя, его можно менять и мы должны его сохранить

//    Устанавливаем пользователю новое имя
        user.setUsername(username);

//     Получаем список ролей, чтобы проверить, что они установлены данному пользователю
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

//     Очищаем массив ролей перед обновлением
        user.getRoles().clear();

//      Теперь нужно проверить, что форма содержит роли для данного пользователя
//      и выбрать из всех полей формы именно роли
        for (String key : form.keySet()){
            if (roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(user);

    }

    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();
        boolean isEmailChanged = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email));

        if (isEmailChanged) {
            user.setEmail(email);

            if (!StringUtils.isEmpty(email)) {
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }

        if (!StringUtils.isEmpty(password)) {
            user.setPassword(password);
        }

        userRepo.save(user);

        if (isEmailChanged) {
            sendMessage(user);
        }

    }
}
