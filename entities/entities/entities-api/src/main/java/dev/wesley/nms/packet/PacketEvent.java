package dev.wesley.nms.packet;

import dev.wesley.nms.packet.types.PacketPlayInBlockDigImpl;
import net.minecraft.network.protocol.game.PacketPlayInBlockDig;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public abstract class PacketEvent<T> implements Cancellable {
    private boolean canceled = false;
    private Player player;
    private T packet;

    public PacketEvent(Player player, T packet) {
        this.player = player;
        this.packet = packet;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    public T getPacket() {
        return packet;
    }

    public void setPacket(T packet) {
        this.packet = packet;
    }

    public static PacketEvent<?> get(Player player, Object o) {

         if (o instanceof PacketPlayInBlockDig) {
            return new PacketPlayInBlockDigImpl(player, (PacketPlayInBlockDig) o);
         }

        return null;
    }
}