package com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui;

import com.github.zr0n1.multiproto.gui.ChangeVersionScreen;
import com.github.zr0n1.multiproto.mixinterface.MultiprotoServerData;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.telvarost.mojangfixstationapi.client.gui.CallbackButtonWidget;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.EditServerScreen;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.MultiplayerScreen;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.ServerData;

@Mixin(EditServerScreen.class)
public class EditServerScreenMixin extends Screen {

    @Shadow(remap = false)
    private @Final ServerData server;

    @Shadow private TextFieldWidget nameTextField;

    @Shadow private TextFieldWidget ipTextField;

    @Shadow private ButtonWidget button;


    @Shadow(remap = false)
    private @Final MultiplayerScreen parent;

    @Inject(method = "init", at = @At(value = "INVOKE",
            target = "Lpl/telvarost/mojangfixstationapi/client/gui/multiplayer/EditServerScreen;updateButton()V"))
    private void buttonStuff(CallbackInfo ci) {
        buttons.remove(button);
        buttons.add(button = new CallbackButtonWidget(this.width / 2 - 100, this.height / 4 + 96 + 12, this.server == null ?
                I18n.getTranslation("multiplayer.mojangfixstationapi.addServer") :
                I18n.getTranslation("multiplayer.mojangfixstationapi.edit"), (button) -> {
            if (server != null) {
                server.setName(this.nameTextField.getText());
                server.setIp(this.ipTextField.getText());
            } else {
                parent.getServersList().add(MultiprotoServerData.constructor(nameTextField.getText(), ipTextField.getText(),
                        ProtocolVersionManager.getCurrentVersion()));
            }
            this.parent.saveServers();
            this.minecraft.setScreen(this.parent);
        }));
        buttons.add(new CallbackButtonWidget(width / 2 - 100, height / 4 + 72 + 12,
            I18n.getTranslation("multiproto.gui.changeVersion") + ": " + (server != null ?
                    ((MultiprotoServerData)server).getVersion().nameRange(true) :
                    ProtocolVersionManager.getCurrentVersion().nameRange(true)), (button) -> {
                minecraft.setScreen(new ChangeVersionScreen(this, server));
        }));
    }
}
