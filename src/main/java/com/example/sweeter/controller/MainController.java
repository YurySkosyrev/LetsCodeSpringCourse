package com.example.sweeter.controller;

import com.example.sweeter.domain.Message;
import com.example.sweeter.domain.User;
import com.example.sweeter.domain.dto.MessageDto;
import com.example.sweeter.repository.MessageRepo;
import com.example.sweeter.repository.UserRepo;
import com.example.sweeter.service.MessageService;
import com.example.sweeter.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;


/**
 * Главный контроллер
 * Обработка get-запроса с отображением всех записей
 * Обработка post-запроса на добавление новой записи
 * Обработка post-запроса на отображение записей по фильтру.
 */

@Controller
public class MainController {

    private final UserService userService;
    private final MessageService messageService;

    public MainController(UserService userService,
                          MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    // Spring найдёт в application.properties переменную upload.path и подставит её в ${uploadPath}
    @Value("${upload.path}")
    private String uploadPath;


    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    /**
     * Главный контроллер
     * Обработка get-запроса с отображением всех записей
     * Обработка post-запроса на добавление новой записи
     * Обработка post-запроса на отображение записей по фильтру.
     */

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter,
                       Model model,
                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                       //SQL не гарантирует отсортированную выборку, поэтому сортируем по id и указываем порядок
                       @AuthenticationPrincipal User user) {
        Page<MessageDto> page = messageService.messageList(pageable, filter, user);

        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String addMessage(
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

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = UtilsController.getErrors(bindingResult);
            model.mergeAttributes(errorsMap); // кладём ошибки в модель для отображения
            model.addAttribute("message", message);
        } else {
            saveFile(file, message);
            // при успешной валидации необходимо удалить у model атрибут message, чтобы после ввода сообщения
            // данные не отображались на форме ввода
            model.addAttribute("message", null);

            messageService.saveMessage(message);
        }

        Page<MessageDto> page = messageService.messageList(pageable, "", user);

        model.addAttribute("page", page);
        model.addAttribute("url", "/main");

        return "main";
    }

    @GetMapping("/user-messages/{author}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable(name = "author") User varUser,
            Model model,
            @RequestParam(required = false) Message message,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        User author = userService.getUserById(varUser.getId());

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
            @PathVariable(name = "user") Long user,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (message.getAuthor().equals(currentUser)) {
            if (!text.isEmpty()) {
                message.setText(text);
            }

            if (!tag.isEmpty()) {
                message.setTag(tag);
            }

            saveFile(file, message);

            messageService.saveMessage(message);
        }
        return "redirect:/user-messages/" + user;
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

}