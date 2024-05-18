package com.github.zr0n1.multiproto.parity.hmifabric;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.parity.ItemParityHelper;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import net.glasslauncher.hmifabric.event.HMIItemListRefreshEvent;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class HMIItemListRefreshListener implements HMIItemListRefreshEvent {

    @Override
    public void refreshItemList(ArrayList<ItemStack> stacks) {
        stacks.removeIf(stack -> ItemParityHelper.removed.contains(stack.getItem()) ||
                (Multiproto.getVersion().compareTo(ProtocolVersion.BETA_8) < 0) && stack.getDamage() > 0);
    }
}
