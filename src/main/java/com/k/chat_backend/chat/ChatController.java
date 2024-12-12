package com.k.chat_backend.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void processMessage( @Payload ChatMessage chatMessage) {
        ChatMessage savedMessage = new ChatMessage();
        simpMessagingTemplate.convertAndSendToUser(
                chatMessage.getReceiverEmail(),
                "/queue/messages",
                new ChatNotification(
                      savedMessage.getId(),
                        savedMessage.getSenderEmail(),
                        savedMessage.getReceiverEmail(),
                        savedMessage.getContent()
                ));
    }

    @GetMapping("/messages/{senderEmail}/{recipientEmail}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderEmail,
                                                              @PathVariable String recipientEmail) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderEmail, recipientEmail));
    }

}
