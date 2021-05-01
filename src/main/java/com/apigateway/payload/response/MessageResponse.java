package com.apigateway.payload.response;

public class MessageResponse<T> {

	private T message;

	public MessageResponse(T message) {
		super();
		this.message = message;
	}

	public T getMessage() {
		return message;
	}

	public void setMessage(T message) {
		this.message = message;
	}
	
	
}
