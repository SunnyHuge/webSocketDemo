package com.miqtech.netty.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * WebSocket 消息体
 *
 * @author zhangyuqi
 * @create 2017年07月15日
 */
public class WebSocketMessage<T> {

	@JsonProperty("title")
	private final String title;// 请求消息标题

	@JsonProperty("messageType")
	private final Integer messageType;// 消息类型：1-聊天

	@JsonProperty("sendType")
	private final Integer sendType;// 发送类型：1-群发，2-单发，默认群发

	@JsonProperty("payload")
	private final T payload;// 消息内容 Key (JSON里）

	/**
	 * 无参构造器
	 */
	public WebSocketMessage() {
		this.title = null;
		this.messageType = null;
		this.sendType = null;
		this.payload = null;
	}

	public WebSocketMessage(String title, Integer messageType, Integer sendType, T payload) {
		this.title = title;
		this.messageType = messageType;
		this.sendType = sendType;
		this.payload = payload;
	}

	public String getTitle() {
		return title;
	}

	public Integer getMessageType() {
		return messageType;
	}

	public Integer getSendType() {
		return sendType;
	}

	public T getPayload() {
		return payload;
	}
}
