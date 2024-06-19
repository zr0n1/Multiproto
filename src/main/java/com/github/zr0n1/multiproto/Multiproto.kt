package com.github.zr0n1.multiproto;

import com.github.zr0n1.multiproto.parity.MultiplayerClientPlayerOnLadderHandler;
import com.github.zr0n1.multiproto.protocol.Versions;
import net.glasslauncher.mods.api.gcapi.api.GConfig;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.network.MultiplayerClientPlayerEntity;
import net.modificationstation.stationapi.api.event.entity.player.PlayerEvent;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

public class Multiproto {

    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();
    @Entrypoint.Logger("Multiproto")
    public static final Logger LOGGER = Null.get();
    @GConfig(value = "config", visibleName = "Multiproto Config")
    public static final Config config = new Config();

    @EventListener
    void registerPlayerHandlers(PlayerEvent.HandlerRegister event) {
        if (event.player instanceof MultiplayerClientPlayerEntity player) {
            event.playerHandlers.add(new MultiplayerClientPlayerOnLadderHandler(player));
        }
    }

    @EventListener
    void registerProtocolVersions(PacketRegisterEvent event) {
        Versions.registerVersions();
    }
}
