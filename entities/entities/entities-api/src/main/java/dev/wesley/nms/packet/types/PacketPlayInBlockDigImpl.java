package dev.wesley.nms.packet.types;

import dev.wesley.nms.packet.PacketEvent;
import net.minecraft.network.protocol.game.PacketPlayInBlockDig;
import org.bukkit.entity.Player;

public class PacketPlayInBlockDigImpl extends PacketEvent<PacketPlayInBlockDig> {

    public PacketPlayInBlockDigImpl(Player player, PacketPlayInBlockDig packet) {
        super(player, packet);
    }

    /*
    TODO The packet mapper for fields (must be cross version)
     */
}