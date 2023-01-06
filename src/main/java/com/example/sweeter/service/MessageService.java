package com.example.sweeter.service;

import com.example.sweeter.domain.Message;
import com.example.sweeter.repository.MessageRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final MessageRepo messageRepo;


    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public Page<Message> messageList(Pageable pageable, String filter){

        if(filter != null && !filter.isEmpty()) {
            return messageRepo.findByTag(filter, pageable);
        } else {
            return messageRepo.findAll(pageable);
        }

    }
}
