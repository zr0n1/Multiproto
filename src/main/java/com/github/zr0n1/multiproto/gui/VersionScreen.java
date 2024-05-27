package com.github.zr0n1.multiproto.gui;

import com.github.zr0n1.multiproto.mixin.MultiprotoMixinPlugin;
import com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui.DirectConnectScreenAccessor;
import com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui.EditServerScreenAccessor;
import com.github.zr0n1.multiproto.mixinterface.MultiprotoServerData;
import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import com.github.zr0n1.multiproto.protocol.Versions;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.DirectConnectScreen;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.EditServerScreen;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.ServerData;

import java.util.Comparator;
import java.util.List;

public class VersionScreen extends Screen {

    private static Version lastServerVersion;
    private final Screen parent;
    private final ServerData server;
    private final List<Version> versions;
    //private final List<ProtocolVersion> alphaVersions;
    //private final List<ProtocolVersion> betaVersions;

    public VersionScreen(Screen parent, ServerData server) {
        this.parent = parent;
        this.server = server;
        this.versions = Versions.VERSIONS.stream().sorted(Comparator.reverseOrder()).toList();
        //this.alphaVersions = ProtocolVersion.ALPHA_PROTOCOL_VERSIONS.stream().sorted(Comparator.reverseOrder()).toList();
        //this.betaVersions = ProtocolVersion.BETA_PROTOCOL_VERSIONS.stream().sorted(Comparator.reverseOrder()).toList();
    }

    public VersionScreen(Screen parent) {
        this(parent, null);
    }

    public static Version getLastServerVersion() {
        return lastServerVersion != null ? lastServerVersion : VersionManager.getLastVersion();
    }

    public void init() {
        /* alpha not implemented
        for(int i = 0; i < alphaVersions.size(); i++) {
            ProtocolVersion a = alphaVersions.get(i);
            ButtonWidget button = new ButtonWidget(versions.indexOf(a), (i % 2 == 0) ? width / 2 - 184 : width / 2 + 4,
                    height / 4 - 24 + 12 * (i - i % 2), 180, 20, a.nameRange(false));
            if(i == alphaVersions.size() - 1 && i % 2 == 0) button.x = width / 2 - 90;
            button.visible = false;
            button.active = false;
            buttons.add(button);
        }
         */
        for (Version version : versions) {
            int i = versions.indexOf(version);
            int size = versions.size();
            boolean side = i < size / 2;
            ButtonWidget button = new ButtonWidget(i, side ? width / 2 - 184 : width / 2 + 4,
                    height / 4 - 12 + 24 * (side ? i : i - size / 2),
                    180, 20, version.nameRange(false));
            if (i == size - 1 && i % 2 == 0) button.x = width / 2 - 90;
            buttons.add(button);
        }
        buttons.add(new ButtonWidget(100, width / 2 - 100, ((ButtonWidget) buttons.get(versions.size() - 1)).y + 36,
                I18n.getTranslation("gui.cancel")));
//        buttons.add(new ButtonWidget(101, width / 2 - 100, height / 4 + 120 - 36, "Change page"));
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        if (button.id == 100) {
            minecraft.setScreen(parent);
        } else if (button.id == 101) {
            for (int i = 0; i < versions.size(); i++) {
                ButtonWidget b = (ButtonWidget) buttons.get(i);
                b.active = !b.active;
                b.visible = !b.visible;
            }
        } else if (button.id <= versions.size()) {
            Version version = versions.get(button.id);
            if (MultiprotoMixinPlugin.shouldApplyMojangFixStAPIServerListIntegration()) {
                if (parent instanceof DirectConnectScreen) {
                    String address = ((DirectConnectScreenAccessor) parent).getAddressField().getText();
                    boolean active = ((DirectConnectScreenAccessor) parent).getConnectButton().active;
                    VersionManager.setLastVersion(version);
                    minecraft.setScreen(parent);
                    ((DirectConnectScreenAccessor) parent).getAddressField().setText(address);
                    ((DirectConnectScreenAccessor) parent).getConnectButton().active = active;
                } else if (parent instanceof EditServerScreen) {
                    String name = ((EditServerScreenAccessor) parent).getNameTextField().getText();
                    String address = ((EditServerScreenAccessor) parent).getIpTextField().getText();
                    boolean active = ((EditServerScreenAccessor) parent).getButton().active;
                    lastServerVersion = version;
                    if (server != null) ((MultiprotoServerData) server).multiproto_setVersion(lastServerVersion);
                    minecraft.setScreen(parent);
                    ((EditServerScreenAccessor) parent).getNameTextField().setText(name);
                    ((EditServerScreenAccessor) parent).getIpTextField().setText(address);
                    ((EditServerScreenAccessor) parent).getButton().active = active;
                }
            } else {
                VersionManager.setLastVersion(version);
                minecraft.setScreen(parent);
            }
        }
    }

    public void render(int x, int y, float delta) {
        renderBackground();
        drawCenteredTextWithShadow(textRenderer, I18n.getTranslation("Protocol version"),
                width / 2, 20, 16777215);
        super.render(x, y, delta);
    }
}
