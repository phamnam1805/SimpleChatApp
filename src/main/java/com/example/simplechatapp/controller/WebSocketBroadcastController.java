package com.example.simplechatapp.controller;

import com.example.simplechatapp.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebSocketBroadcastController {
    @GetMapping("/stomp-broadcast")
    public String getWebSocketBroadcast() {
        return "stomp-broadcast";
    }

    @GetMapping("/sockjs-broadcast")
    public String getSockJsBroadcast() {
        return "sockjs-broadcast";
    }

    @MessageMapping("/broadcast") // endpoint ma nguoi dung gui tin nhan
    @SendTo("/topic/messages") // endpoint nguoi dung subcribe de nhan tin nhan
    public ChatMessage send(ChatMessage chatMessage) throws Exception {
//        System.out.println(chatMessage.getFrom()+": "+chatMessage.getText());
        return new ChatMessage(chatMessage.getFrom(), chatMessage.getText(), "ALL");
    }
}
