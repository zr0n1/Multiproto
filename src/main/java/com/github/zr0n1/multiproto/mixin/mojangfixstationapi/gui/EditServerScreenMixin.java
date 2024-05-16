package com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.gui.ChangeVersionScreen;
import com.github.zr0n1.multiproto.mixinterface.MultiprotoServerData;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.telvarost.mojangfixstationapi.client.gui.CallbackButtonWidget;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.EditServerScreen;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.MultiplayerScreen;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.ServerData;

import java.util.function.Consumer;

@Mixin(EditServerScreen.class)
public class EditServerScreenMixin extends Screen {

    @Shadow(remap = false)
    private @Final ServerData server;
    @Shadow(remap = false)
    private TextFieldWidget nameTextField;
    @Shadow(remap = false)
    private TextFieldWidget ipTextField;


    @Shadow(remap = false)
    private @Final MultiplayerScreen parent;

    @ModifyArg(method = "init", at = @At(value = "INVOKE",
            target = "Lpl/telvarost/mojangfixstationapi/client/gui/CallbackButtonWidget;<init>(IILjava/lang/String;Ljava/util/function/Consumer;)V",
            ordinal = 0),
            index = 3)
    private Consumer<CallbackButtonWidget> modifyCallbackButton(Consumer<CallbackButtonWidget> onPress) {
        return (b) -> {
            if(server != null) {
                server.setName(this.nameTextField.getText());
                server.setIp(this.ipTextField.getText());
            } else {
                parent.getServersList().add(MultiprotoServerData.constructor(nameTextField.getText(), ipTextField.getText(),
                        Multiproto.getVersion()));
            }
            this.parent.saveServers();
            this.minecraft.setScreen(this.parent);
        };
    }

    @Inject(method = "init", at = @At(value = "RETURN"))
    private void addButtons(CallbackInfo ci) {
        buttons.add(new CallbackButtonWidget(width / 2 - 100, height / 4 + 72 + 12,
            I18n.getTranslation("multiproto.gui.changeVersion") + ": " + (server != null ?
                    ((MultiprotoServerData)server).getVersion().nameRange(true) :
                    Multiproto.getVersion().nameRange(true)), (button) -> {
                minecraft.setScreen(new ChangeVersionScreen(this, server));
        }));
    }
}
