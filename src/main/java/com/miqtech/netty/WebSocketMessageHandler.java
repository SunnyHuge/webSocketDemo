package com.miqtech.netty;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.miqtech.netty.message.ChatMessage;
import com.miqtech.netty.message.WebSocketMessage;
import com.miqtech.netty.util.JsonUtils;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * webSocket 消息处理
 *
 * @author zhangyuqi
 * @create 2017年07月15日
 */
public class WebSocketMessageHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketMessageHandler.class);

	/**
	 * 私有构造器
	 */
	private WebSocketMessageHandler() {

	}

	/**
	 * 匿名内部类实现单列模式
	 */
	private static class MessageHandlerHolder {
		private static final WebSocketMessageHandler INSTANCE = new WebSocketMessageHandler();
	}

	/**
	 * 获取单列方法
	 */
	public static WebSocketMessageHandler getInstance() {
		return MessageHandlerHolder.INSTANCE;
	}

	// 当前房间的用户客户端信息
	private Map<Long, ChannelGroup> groupRooms = new ConcurrentHashMap<Long, ChannelGroup>();

	public String messageHandler(Channel channel, final WebSocketMessage<JsonNode> node) {
		switch (node.getMessageType()) {
		case Constants.MESSAGE_TYPE_CHAT:
			return chatMessageHandler(channel, node.getPayload());
		default:
			return null;
		}
	}

	/**
	 * 聊天信息处理
	 */
	private String chatMessageHandler(Channel channel, JsonNode jsonNode) {
		try {
			JavaType javaType = JsonUtils.OBJECT_MAPPER.getTypeFactory().constructParametricType(ChatMessage.class,
					JsonNode.class);
			ChatMessage<JsonNode> chatNode = JsonUtils.OBJECT_MAPPER.readValue(jsonNode.toString(), javaType);

			switch (chatNode.getType()) {
			case Constants.TYPE_IN_ROOM:
				return getIntoRoom(channel, chatNode.getRoom(), chatNode.getNickName());
			case Constants.TYPE_SAY_ROOM:
				return "{\"nickname\":\"" + chatNode.getNickName() + "\",\"msg\":\" " + chatNode.getContent()
						+ " \",\"type\":\"say\"}";
			case Constants.TYPE_OUT_ROOM:
				return leaveRoom(channel, chatNode.getRoom(), chatNode.getNickName());
			default:
				break;
			}
		} catch (IOException e) {
			LOGGER.error("chat Message进行JSON转换时发生错误: {}.", ExceptionUtils.getMessage(e));
		} catch (Exception e) {
			LOGGER.error("处理聊天信息发生错误：{}", e);
		}
		return null;
	}

	/**
	 * 用户进入房间，保存房间信息
	 */
	private String getIntoRoom(Channel channel, Long roomId, String nickName) {
		if (!groupRooms.containsKey(roomId)) {
			groupRooms.put(roomId, new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE));
		}
		groupRooms.get(roomId).add(channel);
		return "{\"title\":\"系统消息\",\"msg\":\"" + nickName + "加入了房间\",\"type\":\"in\"}";
	}

	/**
	 * 用户离开房间，删除用户信息
	 */
	private String leaveRoom(Channel channel, Long roomId, String nickName) {
		ChannelGroup group = groupRooms.get(roomId);
		group.remove(channel);

		if (group.isEmpty()) {
			groupRooms.remove(roomId);
		}
		return "{\"title\":\"系统消息\",\"msg\":\"" + nickName + "离开了房间\",\"type\":\"out\"}";
	}
}
