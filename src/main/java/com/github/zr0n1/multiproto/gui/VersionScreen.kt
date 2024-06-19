package com.github.zr0n1.multiproto.gui

import com.github.zr0n1.multiproto.mixin.MultiprotoMixinPlugin
import com.github.zr0n1.multiproto.mixin.gui.MultiplayerScreenAccessor
import com.github.zr0n1.multiproto.mixin.gui.ScreenAccessor
import com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui.DirectConnectScreenAccessor
import com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui.EditServerScreenAccessor
import com.github.zr0n1.multiproto.mixinterface.MultiprotoServerData
import com.github.zr0n1.multiproto.protocol.Version
import com.github.zr0n1.multiproto.protocol.VersionRegistry
import com.github.zr0n1.multiproto.protocol.lastVer
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.EntryListWidget
import net.minecraft.client.render.Tessellator
import net.minecraft.client.resource.language.I18n
import pl.telvarost.mojangfixstationapi.client.gui.CallbackButtonWidget

class VersionScreen(private val parent: Screen) : Screen() {
    private lateinit var listWidget: VersionListWidget
    private lateinit var selectButton: ButtonWidget
    private lateinit var selectAndButton: ButtonWidget
    
    private val versions: List<Version> = VersionRegistry.VERSIONS.reversed()

    private val MultiplayerScreen.connectButton: ButtonWidget
        get() = (this as ScreenAccessor).buttons[0]

    override fun init() {
        listWidget = VersionListWidget()
        buttons.add(ButtonWidget(0, width / 2 - 100, height - 28, I18n.getTranslation("gui.cancel")))
        selectButton = ButtonWidget(1, width / 2 - 100, height - 52, 96, 20, "Select")
            .also { buttons.add(it) }
        selectAndButton = ButtonWidget(2,
            width / 2 + 4, height - 52,
            96, 20,
            "Select and " +
                    if (MultiprotoMixinPlugin.shouldApplyMojangFixStAPIMixins() && parent is EditServerScreenAccessor) {
                        if (parent.server != null) "edit" else "add"
                    } else "join"
        ).also { buttons.add(it) }
        selectAndButton.active = parent is MultiplayerScreen && parent.connectButton.active ||
                MultiprotoMixinPlugin.shouldApplyMojangFixStAPIMixins() &&
                (parent is EditServerScreenAccessor && parent.button.active ||
                        parent is DirectConnectScreenAccessor && parent.connectButton.active)
    }

    override fun buttonClicked(button: ButtonWidget) {
        if (!button.active) return
        when (button.id) {
            0 -> closeScreen()
            1 -> selectVersion()
            2 -> {
                selectVersion()
                if (parent is MultiplayerScreen) (parent as ScreenAccessor).invokeButtonClicked(parent.connectButton)
                if (MultiprotoMixinPlugin.shouldApplyMojangFixStAPIMixins()) {
                    if (parent is DirectConnectScreenAccessor) parent.connectButton.onPress()
                    if (parent is EditServerScreenAccessor) (parent.button as CallbackButtonWidget).onPress()
                }
            }
        }
    }

    override fun render(x: Int, y: Int, delta: Float) {
        listWidget.render(x, y, delta)
        drawCenteredTextWithShadow(textRenderer, "Protocol version", width / 2, 20, 16777215)
        super.render(x, y, delta)
    }

    private fun selectVersion() {
        if (MultiprotoMixinPlugin.shouldApplyMojangFixStAPIMixins() && parent is EditServerScreenAccessor) {
            lastSerVer = versions[listWidget.selectedIndex]
            (parent.server as? MultiprotoServerData)?.multiproto_setVersion(lastSerVer)
        } else lastVer = versions[listWidget.selectedIndex]
        closeScreen()
    }

    private fun closeScreen() {
        if (parent is MultiplayerScreenAccessor) {
            val address = parent.addressField.text
            minecraft.setScreen(parent)
            parent.addressField.text = address
        }
        if (MultiprotoMixinPlugin.shouldApplyMojangFixStAPIMixins()) {
            if (parent is DirectConnectScreenAccessor) {
                val address = parent.addressField.text
                val active = parent.connectButton.active
                minecraft.setScreen(parent)
                parent.addressField.text = address
                parent.connectButton.active = active
            } else if (parent is EditServerScreenAccessor) {
                val name = parent.nameTextField.text
                val address = parent.ipTextField.text
                val active = parent.button.active
                minecraft.setScreen(parent)
                parent.nameTextField.text = name
                parent.ipTextField.text = address
                parent.button.active = active
            }
        }
    }

    private inner class VersionListWidget : EntryListWidget(minecraft, width, height, 32, height - 64, 24) {
        var selectedIndex = versions.indexOf(
            if (MultiprotoMixinPlugin.shouldApplyMojangFixStAPIMixins() && parent is EditServerScreenAccessor) {
                (parent.server as? MultiprotoServerData)?.multiproto_getVersion()
            } else lastVer
        )

        override fun entryClicked(index: Int, doubleClick: Boolean) {
            selectedIndex = index
            if (doubleClick) selectVersion()
        }

        override fun renderEntry(index: Int, x: Int, y: Int, l: Int, tesselator: Tessellator) {
            drawCenteredTextWithShadow(textRenderer,
                versions[index].nameRange(),
                width / 2, y + 1, 16777215
            )
            drawCenteredTextWithShadow(textRenderer,
                "Protocol number: ${versions[index].protocol}",
                width / 2, y + 12, 8421504
            )
        }

        override fun getEntryCount() = versions.size
        override fun getEntriesHeight() = versions.size * 24
        override fun isSelectedEntry(index: Int) = selectedIndex == index
        override fun renderBackground() = this@VersionScreen.renderBackground()
    }

    companion object {
        @JvmStatic
        private var _lastSerVer: Version? = null

        @JvmStatic
        var lastSerVer: Version
            get() {
                return _lastSerVer ?: lastVer
            }
            set(ver) {
                _lastSerVer = ver
            }
    }
}