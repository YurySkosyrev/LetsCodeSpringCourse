package com.example.sweeter.controller;

import com.example.sweeter.domain.Message;
import com.example.sweeter.domain.User;
import com.example.sweeter.domain.dto.MessageDto;
import com.example.sweeter.repository.MessageRepo;
import com.example.sweeter.repository.UserRepo;
import com.example.sweeter.service.MessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


/**
 * Главный контроллер
 * Обработка get-запроса с отображением всех записей
 * Обработка post-запроса на добавление новой записи
 * Обработка post-запроса на отображение записей по фильтру.
 */

@Controller
public class MessageController {

    private final MessageRepo messageRepo;
    private final UserRepo userRepo;
    private final MessageService messageService;

    public MessageController(MessageRepo messageRepo,
                             UserRepo userRepo,
                             MessageService messageService) {
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;
        this.messageService = messageService;
    }

    @Value("${upload.path}") // Spring найдёт в properties переменную upload.path и подставит её в нашу
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter,
                       Model model,
                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, //SQL не гарантирует отсортированную выборку, поэтому сортируем по id и указываем порядок
                       @AuthenticationPrincipal User user)
    {
        Page<MessageDto> page = messageService.messageList(pageable, filter, user);

        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User user, //huck для проверки приходит ли user в debuge
            @RequestParam("file") MultipartFile file,
            @Valid Message message, //@Valid запускает валидацию
            // так как валидация запущена, нужно получать список аргументов и ошибок валидации - bindingResult,
            // должен идти перед Map иначе все ошибки будут сыпаться во View
            BindingResult bindingResult,
            Model model
    ) throws IOException {

        message.setAuthor(user);

        if (bindingResult.hasErrors()){
            Map<String, String> errorsMap = UtilsController.getErrors(bindingResult);
            model.mergeAttributes(errorsMap); // кладём ошибки в модель для отображения
            model.addAttribute("message", message);
        } else {
            saveFile(file, message);
            // при успешной валидации необходимо удалить у model атрибут message, чтобы после ввода сообщения
            // данные не отображались на форме ввода
            model.addAttribute("message", null);

            messageRepo.save(message);
        }

        Page<MessageDto> page = messageService.messageList(pageable, "", user);

        model.addAttribute("page", page);
        model.addAttribute("url", "/main");

        return "main";
    }

    private void saveFile(MultipartFile file, Message message) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) { // если директория не существует - создаём новую
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString(); // чтобы не было коллизий, создаём уникальное имя файла
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename)); // загрузка файла в файл на диске

            message.setFilename(resultFilename);
        }
    }

    @GetMapping("/user-messages/{author}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable(name = "author") User varUser,
            Model model,
            @RequestParam(required = false) Message message,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ){
        User author = userRepo.getById(varUser.getId());

        Page<MessageDto> page = messageService.messageListForUser(pageable, currentUser, author);

        model.addAttribute("userChannel", author);
        model.addAttribute("subscriptionsCount", author.getSubscriptions().size());
        model.addAttribute("subscribersCount", author.getSubscribers().size());
        model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser));
        model.addAttribute("page", page);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(author));
        model.addAttribute("url", "/user-messages/" + author.getId());

        return "userMessages";
    }

    @PostMapping("/user-messages/{user}")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable(name="user") Long user,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if(message.getAuthor().equals(currentUser)){
            if(!text.isEmpty()){
                message.setText(text);
            }

            if(!tag.isEmpty()){
                message.setTag(tag);
            }

            saveFile(file, message);

            messageRepo.save(message);
        }
        return "redirect:/user-messages/" + user;
    }

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

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build(); // используется для извлечения параметров с той страницы, откуда мы пришли

        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

        return "redirect:" + components.getPath();
    }

}