package com.splus.websocket;

import java.util.List;

import org.springframework.web.socket.WebSocketSession;

public interface WebSocketService {
	
	void addWebsocketSession(String token,WebSocketSession session);
	void removeWebsocketSession(String token);
	boolean sendTextMessage(String token,String message);
	void sendTextMessage(List<String> tokenList,String message);
}
