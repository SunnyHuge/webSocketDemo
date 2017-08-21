package com.miqtech.netty.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 游戏账户信息
 *
 * @author zhangyuqi
 * @create 2017年07月22日
 */
public class AccountResultMessage {

	@JsonProperty("room")
	private Long room;// 房间号

	@JsonProperty("result")
	private Integer result;// 账号匹配结果：1-当前玩家匹配成功，2-所有玩家完成匹配，3-账号不足

	//所有玩家账号匹配成功时返回 Map {key-userId(玩家ID)，value:{account(游戏账号)，password(账号密码)，heroId(英雄ID)}}
	//系统账号不足 返回位置信息
	@JsonProperty("payload")
	private Object payload;// 消息内容，根据result返回

	public Long getRoom() {
		return room;
	}

	public void setRoom(Long room) {
		this.room = room;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}
}
