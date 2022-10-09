package dev.wesley.nms.injector;

import dev.wesley.nms.handler.PlayerChannelDuplexHandler;
import dev.wesley.system.events.subscription.EventSubscription;
import dev.wesley.system.events.subscription.EventSubscriptions;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultChannelPromise;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerPacketInjector {

    public static ConcurrentHashMap<Player, PlayerChannelDuplexHandler> playerHandler;

    static {
        playerHandler = new ConcurrentHashMap<>();
    }

    public PlayerPacketInjector() {
        EventSubscriptions.instance.subscribe(this, getClass());
    }

    public PlayerChannelDuplexHandler inject(final Player player) {

        if (!playerHandler.containsKey(player)) {
            final PlayerChannelDuplexHandler handler = new PlayerChannelDuplexHandler(player);
            startListen(player, handler);
            playerHandler.put(player, handler);

            return handler;

        } else {
            return playerHandler.get(player);
        }

    }

    public void unInject(final Player player) {
        if (playerHandler.containsKey(player)) {
            stopListen(player);
            playerHandler.remove(player);
        }

    }

    public static void sendPacket(final Player player, final Object packet) {
        final PlayerChannelDuplexHandler handler = playerHandler.get(player);
        if (handler == null) {
            return;
        }

        try {
            handler.write(handler.getCtx(), packet, new DefaultChannelPromise(handler.getPromise().channel()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventSubscription
    public void playerJoinEvent(final PlayerJoinEvent event) {
        inject(event.getPlayer());

        event.getPlayer().sendMessage("Injected!");
    }

    @EventSubscription
    public void playerQuitEvent(final PlayerQuitEvent event) {
        unInject(event.getPlayer());
    }

    private void startListen(final Player player, final PlayerChannelDuplexHandler duplexHandler) {
        final ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().b.a.k.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), duplexHandler);
    }

    private void stopListen(final Player player) {
        final Channel channel = ((CraftPlayer) player).getHandle().b.a.k;

        if (channel == null) return;

        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
        });
    }

}
