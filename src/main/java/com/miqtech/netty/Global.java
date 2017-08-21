package com.miqtech.netty;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * ${DESCRIPTION}
 *
 * @author zhangyuqi
 * @create 2017年07月13日
 */
public class Global {
	public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
