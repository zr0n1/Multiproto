package com.github.zr0n1.multiproto.protocol.packet;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.api.packet.DataType;
import com.github.zr0n1.multiproto.api.packet.FieldEntry;
import com.github.zr0n1.multiproto.api.packet.PacketWrapper;
import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.login.LoginHelloPacket;
import net.minecraft.network.packet.play.PlayerRespawnPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

import static com.github.zr0n1.multiproto.protocol.packet.PacketTranslator.wrapLE;

public class BetaPackets {

    public static void applyChanges() {
        // <= b1.6
        wrapLE(Version.BETA_13, new PacketWrapper<LoginHelloPacket>(1,
                FieldEntry.of(DataType.INT, p -> VersionManager.getVersion().protocol), // protocol
                FieldEntry.of(DataType.string(16)),// username
                VersionManager.isLE(Version.BETA_11) ? FieldEntry.unique(DataType.UTF, "Password") : null,
                FieldEntry.of(DataType.LONG), // seed
                VersionManager.isLE(Version.ALPHA_2) ? FieldEntry.dummy(0) : FieldEntry.of(DataType.BYTE) // dimension
        ).postWrite(hello -> Multiproto.LOGGER.info("Logged in as {} on protocol version {}",
                hello.username, hello.protocolVersion)));
        // <= b1.5
        wrapLE(Version.BETA_11, new PacketWrapper<EntitySpawnS2CPacket>(23,
                FieldEntry.of(DataType.INT), // id
                FieldEntry.of(DataType.BYTE, 7), // entity type
                FieldEntry.of(DataType.INT), // x
                FieldEntry.of(DataType.INT), // y
                FieldEntry.of(DataType.INT), // z
                FieldEntry.dummy(0), // velocity x
                FieldEntry.dummy(0), // velocity y
                FieldEntry.dummy(0) // velocity z
        ));
        wrapLE(Version.BETA_11, new PacketWrapper<PlayerRespawnPacket>(9));
        // <= b1.4
        wrapLE(Version.BETA_10, new PacketWrapper<ClickSlotC2SPacket>(102,
                FieldEntry.of(DataType.BYTE), // id
                FieldEntry.of(DataType.SHORT), // slot
                FieldEntry.of(DataType.BYTE), // button
                FieldEntry.of(DataType.SHORT), // action type
                FieldEntry.dummy(false, 5), // shift click
                FieldEntry.of(DataType.ITEM_STACK) // itemstack
        ));
    }
}
