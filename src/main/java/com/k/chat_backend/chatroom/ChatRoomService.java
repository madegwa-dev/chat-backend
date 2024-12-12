package com.k.chat_backend.chatroom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    
    private final ChatRoomRepository chatRoomRepository;
    
    public Optional<String> getChatRoomId(String senderEmail, String recipientEmail, boolean createNewChatRoomIfNotExists) {
        return chatRoomRepository.findBySenderEmailAndRecipientEmail(senderEmail, recipientEmail)
                .map(ChatRoom::getChatId)
                .or(() ->{
                    if (createNewChatRoomIfNotExists) {
                        var chatId = createChatId(senderEmail,recipientEmail);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    private String createChatId(String senderEmail, String recipientEmail) {
        var chatId = String.format("%s_%s", senderEmail, recipientEmail);

        ChatRoom senderRecipient =  ChatRoom.builder()
                .chatId(chatId)
                .senderEmail(senderEmail)
                .recipientEmail(recipientEmail)
                .build();

        ChatRoom recipientSender =  ChatRoom.builder()
                .chatId(chatId)
                .senderEmail(recipientEmail)
                .recipientEmail(senderEmail)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }
}
