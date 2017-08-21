package com.miqtech.netty;

/**
 * ${DESCRIPTION}
 *
 * @author zhangyuqi
 * @create 2017年07月15日
 */
public class Constants {

	/* 消息发送类型 */
	public static final Integer SEND_TYPE_ALL = 1;// 群发
	public static final Integer SEND_TYPE_SINGLE = 2;// 单发

	/* 消息类型 */
	public static final int MESSAGE_TYPE_CHAT = 1;// 聊天

	/* 房间活动类型 */
	public static final int TYPE_IN_ROOM = 1;// 进入房间
	public static final int TYPE_SAY_ROOM = 2;// 在房间说话
	public static final int TYPE_OUT_ROOM = 3;// 离开房间

	/* 针对房间活动类型返回消息体 */
	public static final String IN_ROOM = "{\"title\":\"系统消息\",\"msg\":\" {0} 加入了房间\",\"type\":\"in\"}";
	public static final String SAY_ROOM = "{\"nickname\":\" {0} \",\"msg\":\" {1} \",\"type\":\"say\"}";
	public static final String OUT_ROOM = "{\"title\":\"系统消息\",\"msg\":\" {0} 离开了房间\",\"type\":\"out\"}";
}
