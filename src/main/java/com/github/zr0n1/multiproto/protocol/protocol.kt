package com.github.zr0n1.multiproto.protocol

import com.github.zr0n1.multiproto.Multiproto
import com.github.zr0n1.multiproto.api.event.VersionChangedListener
import com.github.zr0n1.multiproto.parity.BlockHelper
import com.github.zr0n1.multiproto.parity.HMIFabricIntegrationHelper
import com.github.zr0n1.multiproto.parity.ItemHelper
import com.github.zr0n1.multiproto.parity.RecipeHelper
import com.github.zr0n1.multiproto.parity.optional.TextureHelper
import com.github.zr0n1.multiproto.parity.optional.TranslationHelper
import com.github.zr0n1.multiproto.protocol.Version.Type
import com.github.zr0n1.multiproto.protocol.packet.*
import com.github.zr0n1.multiproto.util.fabric
import net.minecraft.client.Minecraft
import java.io.*

/**
 * Beta 1.7 - Beta 1.7.3
 */
@JvmField
val BETA_14 = Version(14, Type.BETA, "1.7", "1.7.3")

/**
 * Beta 1.6 - Beta 1.6.6
 */
@JvmField
val BETA_13 = Version(13, Type.BETA, "1.6", "1.6.6")

/**
 * Beta 1.5 - Beta 1.5_01
 */
@JvmField
val BETA_11 = Version(11, Type.BETA, "1.5", "1.5_01")

/**
 * Beta 1.4 - Beta 1.4_01
 */
@JvmField
val BETA_10 = Version(10, Type.BETA, "1.4", "1.4_01")

/**
 * Beta 1.3 - Beta 1.3_01
 */
@JvmField
val BETA_9 = Version(9, Type.BETA, "1.3", "1.3_01")

/**
 * Beta 1.2 - Beta 1.2_02
 */
@JvmField
val BETA_8 = Version(8, Type.BETA, "1.2", "1.2_02")

/**
 * Beta 1.1_02
 */
@JvmField
val BETALPHA_8 = Version(8, Type.BETALPHA, "1.1_02")

/**
 * Beta 1.0 - Beta 1.1_01
 */
@JvmField
val BETALPHA_7 = Version(7, Type.BETALPHA, "1.0", "1.1_01")

/**
 * Alpha v1.2.3_05 - Alpha v1.2.6
 */
@JvmField
val ALPHA_6 = Version(6, Type.ALPHA, "1.2.3_05", "1.2.6")

/**
 * Alpha v1.2.3 - Alpha v1.2.3_04
 */
@JvmField
val ALPHA_5 = Version(5, Type.ALPHA, "1.2.3", "1.2.3_04")

/**
 * Alpha v1.2.2
 */
@JvmField
val ALPHA_4 = Version(4, Type.ALPHA, "1.2.2")

/**
 * Alpha v1.2.0 - Alpha v1.2.1_01
 */
@JvmField
val ALPHA_3 = Version(3, Type.ALPHA, "1.20", "1.2.1_01")

/**
 * Alpha v1.1.1 - Alpha v1.1.2_01
 */
@JvmField
val ALPHA_2 = Version(2, Type.ALPHA, "1.1.1", "1.1.2_01")

private val versionChangedListeners =
    ((arrayListOf(
        PacketTranslator, BetaPackets, BetalphaPackets, AlphaPackets,
        BlockHelper, ItemHelper, RecipeHelper, TextureHelper, TranslationHelper
    ) + fabric.getEntrypoints("multiproto:version_changed", VersionChangedListener::class.java)) as MutableList)
        .also {
            if (fabric.isModLoaded("hmifabric")) it += HMIFabricIntegrationHelper
        }.toList()

/**
 * Current protocol version.
 */
var currVer: Version = BETA_14
    set(version) {
        if (field != version) field = version
        versionChangedListeners.forEach { it() }
    }

private var _lastVer: Version? = null

/**
 * Last protocol version.
 */
var lastVer: Version
    get() {
        if (_lastVer == null) {
            val file = File(Minecraft.getRunDirectory(), "config/multiproto/lastversion.txt")
            if (file.exists()) {
                try {
                    val br = BufferedReader(FileReader(file))
                    val s = br.readLine()
                    br.close()
                    _lastVer = Version.parse(s)
                } catch (e: Exception) {
                    Multiproto.LOGGER.error("Error loading last protocol version")
                    e.printStackTrace()
                }
            }
        }
        return _lastVer ?: BETA_14
    }
    set(ver) {
        if (_lastVer != ver) {
            val file = File(Minecraft.getRunDirectory(), "config/multiproto/lastversion.txt")
            try {
                val pw = PrintWriter(FileWriter(file))
                pw.print(ver.also { _lastVer = it })
                pw.close()
            } catch (e: Exception) {
                Multiproto.LOGGER.error("Error writing last protocol version to text file")
                e.printStackTrace()
            }
        }
    }