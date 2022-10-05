package com.example.sweeter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Приложение основано на Spring MVC.
 * Т.о. нам необходимо настроить Spring MVC и
 * контроллеры представлений для отображения этих шаблонов.
 */



@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       registry.addResourceHandler("/img/**") // каждое обращение к серверу будет перенаправляться по пути
               .addResourceLocations("file:" + uploadPath + "/");
       registry.addResourceHandler("/static/**") // ** - значит путь и всю иерархию
               .addResourceLocations("classpath:static/"); // ресурсы будут искаться не в конкретной директории, а в дереве проекта
    }
}
