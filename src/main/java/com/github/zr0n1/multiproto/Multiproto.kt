package com.github.zr0n1.multiproto

import com.github.zr0n1.multiproto.parity.MultiplayerClientPlayerOnLadderHandler
import com.github.zr0n1.multiproto.protocol.VersionRegistry
import net.glasslauncher.mods.api.gcapi.api.GConfig
import net.mine_diver.unsafeevents.listener.EventListener
import net.minecraft.client.network.MultiplayerClientPlayerEntity
import net.modificationstation.stationapi.api.event.entity.player.PlayerEvent.HandlerRegister
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint
import net.modificationstation.stationapi.api.util.Namespace
import org.apache.logging.log4j.Logger

internal object Multiproto {
    @Entrypoint.Namespace
    @JvmSynthetic
    internal lateinit var NAMESPACE: Namespace

    @Entrypoint.Logger("Multiproto")
    @JvmSynthetic
    internal lateinit var LOGGER: Logger

    @GConfig(value = "config", visibleName = "Multiproto Config")
    @JvmField
    val config = Config()

    @EventListener
    private fun registerPlayerHandlers(event: HandlerRegister) {
        if (event.player is MultiplayerClientPlayerEntity) {
            event.playerHandlers.add(MultiplayerClientPlayerOnLadderHandler(event.player as MultiplayerClientPlayerEntity))
        }
    }

    @EventListener
    private fun registerProtocolVersions(event: PacketRegisterEvent) {
        VersionRegistry.registerAll()
    }
}
