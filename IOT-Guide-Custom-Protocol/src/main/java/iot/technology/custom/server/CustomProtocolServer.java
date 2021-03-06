package iot.technology.custom.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import iot.technology.custom.handler.IotIdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author james mu
 * @date 2020/5/26 22:56
 */
@Slf4j
public class CustomProtocolServer {

    private static final int PORT = 8000;

    public static void main(String[] args) throws Exception {
      NioEventLoopGroup bossGroup = new NioEventLoopGroup();
      NioEventLoopGroup workerGroup = new NioEventLoopGroup();

      final ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap
              .group(bossGroup, workerGroup)
              .channel(NioServerSocketChannel.class)
              .option(ChannelOption.SO_BACKLOG, 1024)
              .childOption(ChannelOption.SO_KEEPALIVE, true)
              .childOption(ChannelOption.TCP_NODELAY, true)
              .childHandler(new ChannelInitializer<NioSocketChannel>() {
                  @Override
                  protected void initChannel(NioSocketChannel ch) throws Exception {
                      ch.pipeline().addLast(new IotIdleStateHandler());
                  }
              });
      bind(serverBootstrap, PORT);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                log.info("{} 端口:{}, 绑定成功!",new Date(), port);
            } else {
                log.error("端口:{}, 绑定失败!");
            }
        });
    }
}
