package com.cruise.project_cruise.webSocketConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.*;


import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

//@Component
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    //웹소켓관련 객체
//	@Bean
//	public ServerEndpointExporter serverEndpointExporter() {
//		return new ServerEndpointExporter();
//	}
//}

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{

	@Autowired
	WebsocketTest2 websocketTest2;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(websocketTest2,"/testSocket")
				.addInterceptors(new HttpSessionHandshakeInterceptor());
	}
}