package com.splus.websocket;

import org.json.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.splus.service.LoginService;
import com.splus.subservice.JsonResponseFormatter;

public class WebsocketEndPoint extends TextWebSocketHandler {
	
	private LoginService loginService;
	private WebSocketService wsService;
	
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	public void setWsService(WebSocketService wsService) {
		this.wsService = wsService;
	}

	@Override
	protected void handleTextMessage(WebSocketSession session,
			TextMessage message) throws Exception {
		
		super.handleTextMessage(session, message);
		
		byte resultCode = 0;
		String responseMsg="";
		JSONObject msgJson = new JSONObject(message.getPayload());
				
		String origin = msgJson.getString("origin");
		
		System.out.println("WsOrigin : "+origin);
		if("login".equals(origin)){
			
			String userName = msgJson.getString("userName");
			String token = msgJson.getString("token");
			String roleName = null;
			try {
				roleName = loginService.getSession(userName, token);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(roleName==null)
				session.close();
			else {
				
				session.getAttributes().put("token", token);
				wsService.addWebsocketSession(token, session);
				resultCode = 1;
				JsonResponseFormatter<String> respObj = new JsonResponseFormatter<String>();
				responseMsg = respObj.parseResponse("login", resultCode, "", "");	
			}
		}
		else
			responseMsg = "Invalid origin";
		TextMessage returnMessage = new TextMessage(responseMsg);
		session.sendMessage(returnMessage);
		
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		super.afterConnectionEstablished(session);
		System.out.println("Connection Established");
		
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session,
			CloseStatus status) throws Exception {
		
		super.afterConnectionClosed(session,status);
		wsService.removeWebsocketSession(session.getAttributes().get("token").toString());
		System.out.println("Connection Closed");
		
	}
	
}
