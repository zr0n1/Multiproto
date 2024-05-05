package com.github.zr0n1.multiproto.gui;

import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.resource.language.I18n;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.ServerData;

import java.util.List;

public class ChangeVersionScreen extends Screen {
    private final Screen parent;
    private final ServerData server;
    //private final VersionListWidget versionListWidget;
    private final List<ProtocolVersion> versions;

    public ChangeVersionScreen(Screen parent, ServerData server) {
        this.parent = parent;
        this.server = server;
        this.versions = ProtocolVersion.PROTOCOL_VERSIONS.stream().toList();
        //this.versionListWidget = new VersionListWidget(this);
    }
    public ChangeVersionScreen(Screen parent) {
        this(parent, null);
    }

    public void init() {
        buttons.add(new Button(100, width / 2 - 100, height / 4 + 136,
                I18n.translate("gui.cancel")));
        for(int i = 0; i < versions.size(); i++) {
            ProtocolVersion v = versions.get(i);
            buttons.add(new Button(i,width / 2 - 100, height / 4 - (24 * i), v.names.range()));
        }
    }

    @Override
    protected void buttonClicked(Button button) {
        if(button.id == 100) {
            minecraft.openScreen(parent);
            return;
        }
        if(button.id <= versions.size()) {
            ProtocolVersion.setCurrentVersion(versions.get(button.id));
            minecraft.openScreen(parent);
        }
    }

    public void render(int x, int y, float delta) {
        //this.versionListWidget.render(x, y, delta);
        renderBackground();
        drawTextWithShadowCentred(textManager, I18n.translate("multiplayer.multiproto:changeVersion"),
                width / 2, 20, 16777215);
        super.render(x, y, delta);
    }

    public TextRenderer getTextManager() {
        return textManager;
    }
}
