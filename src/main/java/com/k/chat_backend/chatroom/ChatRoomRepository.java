package com.k.chat_backend.chatroom;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    Optional<ChatRoom> findBySenderEmailAndRecipientEmail(String senderEmail, String recipientEmail);
}
