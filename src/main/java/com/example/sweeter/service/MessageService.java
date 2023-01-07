package com.example.sweeter.service;

import com.example.sweeter.domain.Message;
import com.example.sweeter.domain.User;
import com.example.sweeter.domain.dto.MessageDto;
import com.example.sweeter.repository.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class MessageService {
    private final MessageRepo messageRepo;


    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public Page<MessageDto> messageList(Pageable pageable, String filter, User user){

        if(filter != null && !filter.isEmpty()) {
            return messageRepo.findByTag(filter, pageable, user);
        } else {
            return messageRepo.findAll(pageable, user);
        }

    }

    public Page<MessageDto> messageListForUser(Pageable pageable, User currentUser, User author) {
        return messageRepo.findByUser(pageable,author,currentUser);
    }
}
