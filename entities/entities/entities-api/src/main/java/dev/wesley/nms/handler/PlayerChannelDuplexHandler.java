package dev.wesley.nms.handler;

import dev.wesley.nms.packet.PacketEvent;
import dev.wesley.system.events.subscription.EventSubscriptions;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;

public class PlayerChannelDuplexHandler extends ChannelDuplexHandler {
    private final Player player;
    private ChannelHandlerContext ctx = null;
    private ChannelPromise promise = null;

    public PlayerChannelDuplexHandler(final Player player) {
        this.player = player;
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        final PacketEvent<?> event = PacketEvent.get(player, msg);
        if (event == null) {
            super.channelRead(ctx, msg);
            return;
        }

        EventSubscriptions.instance.publish(event);
        if (!event.isCancelled()) {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {

        this.ctx = ctx;
        this.promise = promise;

        final PacketEvent<?> event = PacketEvent.get(player, msg);

        if (event == null) {
            super.write(ctx, msg, promise);
            return;
        }

        EventSubscriptions.instance.publish(event);
        if (!event.isCancelled()) {
            super.write(ctx, event.getPacket(), promise);
        }

    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public ChannelPromise getPromise() {
        return promise;
    }
}