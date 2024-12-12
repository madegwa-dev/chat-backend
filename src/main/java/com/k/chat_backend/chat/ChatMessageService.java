package com.k.chat_backend.chat;

import com.k.chat_backend.chatroom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatRoomService chatRoomService;
    private final ChatMessageRepository repository;

    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatRoomService
                .getChatRoomId(chatMessage.getSenderEmail(), chatMessage.getReceiverEmail(), true)
                .orElseThrow(); // You can create your own dedicated exception
        chatMessage.setChatId(chatId);
        repository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(String senderEmail, String recipientEmail) {
        var chatId = chatRoomService.getChatRoomId(senderEmail, recipientEmail, false);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }
}
