package com.miqtech.netty.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 房间活动信息 消息体
 *
 * @author zhangyuqi
 * @create 2017年07月15日
 */
public class ChatMessage<T> {

	@JsonProperty("room")
	private final Long room;// 房间号

	@JsonProperty("type")
	private final Integer type;// 活动类型：1-进入聊天，2-开始聊天，3-结束聊天

	@JsonProperty("nickName")
	private final String nickName;// 聊天者昵称

	@JsonProperty("content")
	private final String content;// 聊天内容

	@JsonProperty("payload")
	private final T payload;// 保留字段

	/**
	 * 无参构造器
	 */
	public ChatMessage() {
		this.room = null;
		this.type = null;
		this.nickName = null;
		this.content = null;
		this.payload = null;
	}

	public ChatMessage(Long room, Integer type, String nickName, String content, T payload) {
		this.room = room;
		this.type = type;
		this.nickName = nickName;
		this.content = content;
		this.payload = payload;
	}

	public Long getRoom() {
		return room;
	}

	public Integer getType() {
		return type;
	}

	public String getNickName() {
		return nickName;
	}

	public String getContent() {
		return content;
	}

	public T getPayload() {
		return payload;
	}
}
