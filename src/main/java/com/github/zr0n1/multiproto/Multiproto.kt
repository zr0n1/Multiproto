package com.github.zr0n1.multiproto

import com.github.zr0n1.multiproto.parity.MultiplayerOnLadderHandler
import com.github.zr0n1.multiproto.parity.VersionParity
import net.glasslauncher.mods.api.gcapi.api.GConfig
import net.mine_diver.unsafeevents.listener.EventListener
import net.minecraft.client.network.MultiplayerClientPlayerEntity
import net.modificationstation.stationapi.api.event.entity.player.PlayerEvent
import net.modificationstation.stationapi.api.event.registry.AfterBlockAndItemRegisterEvent
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

    @Suppress("UNUSED_PARAMETER")
    @EventListener
    private fun initJuice(event: AfterBlockAndItemRegisterEvent) {
        VersionParity.BASE_BLOCK_JUICE
        VersionParity.BASE_ITEM_JUICE
    }

    @EventListener
    private fun registerPlayerHandlers(event: PlayerEvent.HandlerRegister) {
        if (event.player is MultiplayerClientPlayerEntity) {
            event.playerHandlers.add(MultiplayerOnLadderHandler(event.player as MultiplayerClientPlayerEntity))
        }
    }
}
