package com.github.zr0n1.multiproto.protocol.packet;

import com.github.zr0n1.multiproto.api.packet.PacketWrapper;
import com.github.zr0n1.multiproto.protocol.Version;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;

import static com.github.zr0n1.multiproto.protocol.packet.PacketTranslator.replaceLE;

public class AlphaPackets {

    public static void applyChanges() {
        replaceLE(Version.ALPHAWEEN_6, new PacketWrapper<>(5, new Object() {
            int type;
            ItemStack[] stacks;
        }).infer().apply((data, handler) -> {
            PlayerEntity player = PlayerHelper.getPlayerFromGame();
            if (data.type == -1) {
                player.inventory.main = data.stacks;
            } else if (data.type == -2) {
                for (int i = 0; i < data.stacks.length; i++) {
                    ((PlayerScreenHandler) player.container).craftingInput.setStack(0, data.stacks[i]);
                }
            } else if (data.type == -3) {
                player.inventory.armor = data.stacks;
            }
        }));
    }
}
