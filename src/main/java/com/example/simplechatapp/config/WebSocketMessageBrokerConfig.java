package com.example.simplechatapp.config;

import com.example.simplechatapp.Utils.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfig implements WebSocketMessageBrokerConfigurer{
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/user"); // endpoint ma nguoi dung subcribe de nhan tin nhan
        config.setApplicationDestinationPrefixes("/app"); // endpoint ma nguoi dung gui tin nhan
//        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/broadcast"); // dang ky endpoint broadcast -> nguoi dung co the gui tin nhan tu /api/broadcast
        registry.addEndpoint("/broadcast").withSockJS().setHeartbeatTime(60_000);;
        registry.addEndpoint("/chat").withSockJS();
    }

    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new UserInterceptor());
    }
}
