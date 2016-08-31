package com.zsq.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.logging.Logger;

public class Client {
	public void connect(int port, String host) throws InterruptedException{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).
			option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel arg0) throws Exception {
					arg0.pipeline().addLast(new ClientHandler());
				}
			} );
		ChannelFuture f = b.connect(host,port).sync();
		f.channel().closeFuture().sync();
		}
		finally{
			group.shutdownGracefully();
		}
	}
	public static void main(String[] args) throws InterruptedException{
		int port = 8080;
		System.out.println("client is runing");
		new Client().connect(port, "127.0.0.1");
	}
}


class ClientHandler extends ChannelHandlerAdapter{
	private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());
	private final ByteBuf firstMsg;
	public ClientHandler(){
		byte[] req = "hello".getBytes();
		firstMsg = Unpooled.buffer(req.length);
		firstMsg.writeBytes(req);
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.writeAndFlush(firstMsg);
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// TODO Auto-generated method stub
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req,"UTF-8");
		System.out.println("now is "+body);
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		logger.warning("unexcepted exception" + cause.getMessage());
		ctx.close();
	}
}



