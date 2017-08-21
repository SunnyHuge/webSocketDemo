package com.miqtech.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * ${DESCRIPTION}
 *
 * @author zhangyuqi
 * @create 2017年07月13日
 */
public class NettyServer {

	private final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

	private static final int PORT = 7397;
	/**用于分配处理业务线程的线程组个数 */
	protected static final int BIZ_GROUP_SIZE = Runtime.getRuntime().availableProcessors() * 2; //默认
	/** 业务出现线程大小*/
	protected static final int BIZ_THREAD_SIZE = 4;

	private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZ_GROUP_SIZE);
	private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZ_THREAD_SIZE);

	@PostConstruct
	public void init() {

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.childHandler(new ChildChannelHandler());

			LOGGER.info("服务端开启等待客户端连接...");

			Channel ch = b.bind(PORT).sync().channel();

			ch.closeFuture().sync();

		} catch (Exception e) {
			LOGGER.error("webSocket服务端启动异常：{}", e);
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}

	//@PreDestroy
	//public void destroy() {
	//	bossGroup.shutdownGracefully();
	//	workerGroup.shutdownGracefully();
	//}

	public static void main(String[] args) {
		new NettyServer().init();
	}
}
