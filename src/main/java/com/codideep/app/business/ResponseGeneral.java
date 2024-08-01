package com.codideep.app.business;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseGeneral<T> {
	private String type;
	private List<String> listMessage;
	private T dto;

	public ResponseGeneral() {
		this.type = "error";
		this.listMessage = new ArrayList<>();

		this.dto = null;
	}

	public void addResponseMesssage(String message) {
		this.listMessage.add(message);
	}
}