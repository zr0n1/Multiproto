package com.github.zr0n1.multiproto.parity

import com.github.zr0n1.multiproto.Multiproto
import com.github.zr0n1.multiproto.api.event.VersionChangedListener
import com.github.zr0n1.multiproto.mixin.parity.block.BlockAccessor
import com.github.zr0n1.multiproto.protocol.BETA_11
import com.github.zr0n1.multiproto.protocol.BETA_13
import com.github.zr0n1.multiproto.protocol.currVer
import net.minecraft.block.Block
import net.minecraft.block.material.Material

object BlockHelper : VersionChangedListener {
    override fun invoke() {
        // set cobweb fields
        (Block.COBWEB as BlockAccessor).setMaterial(if (currVer <= BETA_13) Material.WOOL else Material.COBWEB)
        Block.COBWEB.setHardness(if (currVer <= BETA_13) 0f else 4f)
        Block.COBWEB.setOpacity(if (currVer <= BETA_13) 0 else 1)
        Block.BLOCKS_OPAQUE[Block.COBWEB.id] = currVer <= BETA_13
        // set glowstone fields
        (Block.GLOWSTONE as BlockAccessor).setMaterial(if (currVer <= BETA_11) Material.GLASS else Material.STONE)
        Block.GLOWSTONE.setHardness(if (currVer <= BETA_13) 0.1f else 0.3f)
        Multiproto.LOGGER.info("Applied version block parity")
    }
}
