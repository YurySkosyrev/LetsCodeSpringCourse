package com.example.sweeter.controller;

import com.example.sweeter.domain.Message;
import com.example.sweeter.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

/**
 * Контроллер для работы с лайками
 */

@Controller
public class LikeController {

    @GetMapping("/messages/{message}/like")
    public String like(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Message message,
            RedirectAttributes redirectAttributes, // позволяют пробросить аргументы в метод в который мы делаем redirect
            @RequestHeader(required = false) String referer // получаем страницу, с которой пришли с лайком
    ) {
        Set<User> likes = message.getLikes();

        if (likes.contains(currentUser)){
            likes.remove(currentUser);
        } else {
            likes.add(currentUser);
        }

        // используется для извлечения параметров с той страницы, откуда мы пришли
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

        return "redirect:" + components.getPath();
    }
}
