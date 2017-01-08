package com.splus.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketServiceImp implements WebSocketService {
	
	private static HashMap<String,WebSocketSession> wsSessionList = new HashMap<String,WebSocketSession>();

	@Override
	public void addWebsocketSession(String token, WebSocketSession session) {
		
		wsSessionList.put(token, session);
	}

	@Override
	public void removeWebsocketSession(String token) {
		
		wsSessionList.remove(token);
	}
	
	@Override
	public boolean sendTextMessage(String token, String message) {
		
		TextMessage wsMsg = new TextMessage(message);
		WebSocketSession session = wsSessionList.get(token);
		if(session !=null && session.isOpen())
			try {
				session.sendMessage(wsMsg);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		return false;
	}

	@Override
	public void sendTextMessage(List<String> tokenList, String message) {
		
		TextMessage wsMsg = new TextMessage(message);
		for(String token : tokenList){
			WebSocketSession session = wsSessionList.get(token);
			if(session !=null && session.isOpen())
				try {
					System.out.println("SENDING MSG......"+token);
					session.sendMessage(wsMsg);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}
	
}
