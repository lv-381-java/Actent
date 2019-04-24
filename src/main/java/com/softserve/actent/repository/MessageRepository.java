package com.softserve.actent.repository;

import com.softserve.actent.model.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findAllByChatId(Long chatId, Pageable pageable);

    List<Message> findAllByChatId(Long chatId);

    Long countByChatId(Long chatId);
}
