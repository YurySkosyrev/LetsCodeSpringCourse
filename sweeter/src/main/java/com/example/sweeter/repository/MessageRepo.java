package com.example.sweeter.repository;

import com.example.sweeter.domain.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepo extends CrudRepository<Message, Long> {


}
