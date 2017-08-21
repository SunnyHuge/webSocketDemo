package com.miqtech.netty;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.miqtech.netty.entity.WebSocketMessage;
import com.miqtech.netty.util.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * webSocket服务端
 *
 * @author zhangyuqi
 * @create 2017年07月13日
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

	private final Logger LOGGER = LoggerFactory.getLogger(WebSocketServerHandler.class);

	private static final String REQUEST_HEADER_UPGRADE = "Upgrade";
	private static final String WEB_SOCKET = "webSocket";
	private static final String WEB_SOCKET_URL = "ws://localhost:7397/webSocket";

	private WebSocketServerHandshaker handshake;

	/**
	 * 服务端监听到客户端活动
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Global.group.add(ctx.channel());
		LOGGER.debug("客户端 {}-{} 与服务端连接开启...", ctx.name(), ctx.channel().id());
	}

	/**
	 * 服务端监听到客户端不活动
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Global.group.remove(ctx.channel());
		LOGGER.debug("客户端 {}-{} 与服务端连接关闭！", ctx.name(), ctx.channel().id());
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		LOGGER.debug("服务端读取客户端 {}-{} 完毕！", ctx.name(), ctx.channel().id());
		ctx.flush();
	}

	/**
	 * 服务端收到客户端请求消息
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
		if (o instanceof FullHttpRequest) {
			handleHttpRequest(ctx, (FullHttpRequest) o);
		} else if (o instanceof WebSocketFrame) {
			handleWebSocketFrame(ctx, (WebSocketFrame) o);
		}
	}

	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
		if (!req.decoderResult().isSuccess()
				|| (!WEB_SOCKET.equalsIgnoreCase(req.headers().get(REQUEST_HEADER_UPGRADE)))) {
			sendHttpResponse(ctx, req,
					new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		}

		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(WEB_SOCKET_URL, null, false);
		handshake = wsFactory.newHandshaker(req);
		if (handshake == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		} else {
			handshake.handshake(ctx.channel(), req);
		}

	}

	private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
		// 应答客户端
		if (res.status().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
		}
	}

	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		// 判断是否关闭链路的指令
		if (frame instanceof CloseWebSocketFrame) {
			LOGGER.info("收到 close 指令，即将关闭客户端：{}", ctx.channel().remoteAddress());
			handshake.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			return;
		}

		// 判断是否为ping消息
		if (frame instanceof PingWebSocketFrame) {
			LOGGER.info("收到 ping 消息：{}", frame.content());
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}

		// 二进制消息不处理
		if (!(frame instanceof TextWebSocketFrame)) {
			LOGGER.warn("仅支持文本消息，不支持二进制消息");
			throw new UnsupportedOperationException(
					String.format("%s frame types not support", frame.getClass().getName()));
		}

		String message = ((TextWebSocketFrame) frame).text();
		if (StringUtils.isBlank(message)) {
			LOGGER.error("空消息，丢弃");
			return;
		}

		LOGGER.info("服务端收到信息：" + message);
		try {
			JavaType javaType = JsonUtils.OBJECT_MAPPER.getTypeFactory().constructParametricType(WebSocketMessage.class,
					JsonNode.class);
			WebSocketMessage<JsonNode> node = JsonUtils.OBJECT_MAPPER.readValue(message, javaType);
			// 消息处理
			String responseMsg = MessageHandler.getInstance().messageHandler(ctx.channel(), node);

			// 消息回执
			if (StringUtils.isNotBlank(responseMsg)) {
				TextWebSocketFrame tsf = new TextWebSocketFrame(responseMsg);
				if (Constants.SEND_TYPE_SINGLE.equals(node.getSendType())) {
					// 单发
					ctx.channel().writeAndFlush(tsf);
				} else {
					// 群发
					Global.group.writeAndFlush(tsf);
				}
			}
		} catch (IOException e) {
			LOGGER.error("webSocket Message进行JSON转换时发生错误: {}.", ExceptionUtils.getMessage(e));
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.error("客户端 {} 发生异常：{}", ctx.channel().remoteAddress(), cause);
		// 当出现异常就关闭连接
		ctx.close();
	}
}
