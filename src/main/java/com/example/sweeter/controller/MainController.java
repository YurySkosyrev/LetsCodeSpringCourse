package com.example.sweeter.controller;

import antlr.StringUtils;
import com.example.sweeter.domain.Message;
import com.example.sweeter.domain.User;
import com.example.sweeter.repository.MessageRepo;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Set;
import java.util.UUID;


/**
 * Главный контроллер
 * Обработка get-запроса с отображением всех записей
 * Обработка post-запроса на добавление новой записи
 * Обработка post-запроса на отображение записей по фильтру.
 */

@Controller
public class MainController {

    private final MessageRepo messageRepo;

    public MainController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @Value("${upload.path}") // Spring найдёт в properties переменную upload.path и подставит её в нашу
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model){
        Iterable<Message> messages = messageRepo.findAll();

        if(filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String add(
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

        Iterable<Message> messages = messageRepo.findAll();

        model.addAttribute("messages", messages);

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

    @GetMapping("/user-messages/{user}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable(name="user") User user,
            Model model,
            @RequestParam(required = false) Message message
    ){
        Set<Message> messages = user.getMessages();

        model.addAttribute("messages", messages);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
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

}