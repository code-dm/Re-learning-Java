package com.codedm.java.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.List;
import java.util.Map;

/**
 * @author Dongming WU
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;
            System.out.println("请求方法名称:" + req.method().name());
            System.out.println("请求来自:" + ctx.channel().remoteAddress());

            QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
            Map<String, List<String>> parameters = decoder.parameters();
            if (parameters.containsKey("msg")) {
                System.out.println(parameters.get("msg"));
            }

            ByteBuf content = Unpooled.copiedBuffer("{\"result\": \"success\"}", CharsetUtil.UTF_8);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            ctx.writeAndFlush(response);
            ctx.channel().close();

        } else if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            System.out.println(content.toString(CharsetUtil.UTF_8));

        }
    }
}
