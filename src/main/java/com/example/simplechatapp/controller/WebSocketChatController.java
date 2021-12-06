package com.example.simplechatapp.controller;

import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.example.simplechatapp.Utils.ActiveUserChangeListener;
import com.example.simplechatapp.Utils.ActiveUserManager;
import com.example.simplechatapp.dto.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebSocketChatController implements ActiveUserChangeListener {
    @Autowired
    private SimpMessagingTemplate webSocket;

    @Autowired
    private ActiveUserManager activeUserManager;

    @PostConstruct
    private void init() {
        activeUserManager.registerListener(this);
    }

    @PreDestroy
    private void destroy() {
        activeUserManager.removeListener(this);
    }

    @GetMapping("/sockjs-message")
    public String getWebSocketWithSockJs() {
        return "sockjs-message";
    }

    @MessageMapping("/chat")
    public void send(SimpMessageHeaderAccessor sha, @Payload ChatMessage chatMessage) throws Exception {
//        System.out.println(sha.getNativeHeader("sender"));
//        System.out.println(sha.getUser());
        String sender = sha.getNativeHeader("sender").get(0);
        System.out.println(sender + " " + chatMessage.getRecipient());
        ChatMessage message = new ChatMessage(chatMessage.getFrom(), chatMessage.getText(), chatMessage.getRecipient());
        if (!sender.equals(chatMessage.getRecipient())) {
            webSocket.convertAndSendToUser(sender, "/reply", message);
        }
        webSocket.convertAndSendToUser(chatMessage.getRecipient(), "/reply", message);
    }

    @Override
    public void notifyActiveUserChange() {
        Set<String> activeUsers = activeUserManager.getAll();
        webSocket.convertAndSend("/topic/active", activeUsers);
    }
}
