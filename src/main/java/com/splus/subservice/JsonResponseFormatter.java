package com.splus.subservice;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonResponseFormatter<ResultType> {
	
	public String parseResponse(String origin,byte resultCode,
			String message,ResultType result){
		
		JSONObject responseObj = new JSONObject();
		try {
			responseObj.put("result", (result==null)?"":result);
			responseObj.put("message", message);
			responseObj.put("resultCode", resultCode);
			responseObj.put("origin", origin);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//return responseObj.toString();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(responseObj.toString());
		return gson.toJson(je);
	}
	
	public String parseNotificationMessage(String origin,ResultType result){
		
		JSONObject responseObj = new JSONObject();
		try {
			responseObj.put("origin", origin);
			responseObj.put("message", (result==null)?"":result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return responseObj.toString();
		/*
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(responseObj.toString());
		return gson.toJson(je);
		*/
	}

}
