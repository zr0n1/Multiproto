package com.github.zr0n1.multiproto.gui;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.resource.language.I18n;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.ServerData;

import java.util.Comparator;
import java.util.List;

public class ChangeVersionScreen extends Screen {
    private final Screen parent;
    private final ServerData server;
    private final List<ProtocolVersion> versions;
    private final List<ProtocolVersion> alphaVersions;
    private final List<ProtocolVersion> betaVersions;
    private ButtonWidget pageButton;
    private boolean page = true;

    public ChangeVersionScreen(Screen parent, ServerData server) {
        this.parent = parent;
        this.server = server;
        this.versions = ProtocolVersion.PROTOCOL_VERSIONS.stream().sorted(Comparator.reverseOrder()).toList();
        this.alphaVersions = ProtocolVersion.ALPHA_PROTOCOL_VERSIONS.stream().sorted(Comparator.reverseOrder()).toList();
        this.betaVersions = ProtocolVersion.BETA_PROTOCOL_VERSIONS.stream().sorted(Comparator.reverseOrder()).toList();
    }
    public ChangeVersionScreen(Screen parent) {
        this(parent, null);
    }

    public void init() {
        for(int i = 0; i < alphaVersions.size(); i++) {
            ProtocolVersion a = alphaVersions.get(i);
            ButtonWidget button = new ButtonWidget(versions.indexOf(a), (i % 2 == 0) ? width / 2 - 184 : width / 2 + 4,
                    height / 4 - 24 + 12 * (i - i % 2), 180, 20, a.nameRange());
            if(i == alphaVersions.size() - 1 && i % 2 == 0) button.x = width / 2 - 90;
            button.visible = false;
            button.active = false;
            buttons.add(button);
        }
        for(int i = 0; i < betaVersions.size(); i++) {
            ProtocolVersion b = betaVersions.get(i);
            ButtonWidget button = new ButtonWidget(versions.indexOf(b), (i % 2 == 0) ? width / 2 - 184 : width / 2 + 4,
                    height / 4 - 24 + 12 * (i - i % 2), 180, 20, b.nameRange());
            if(i == betaVersions.size() - 1 && i % 2 == 0) button.x = width / 2 - 90;
            buttons.add(button);
        }
        buttons.add(new ButtonWidget(100, width / 2 - 100, height / 4 + 120 + 12,
                I18n.getTranslation("gui.cancel")));
        buttons.add(pageButton =
                new ButtonWidget(101, width / 2 - 100, height / 4 + 120 - 12, ProtocolVersion.Type.ALPHA.label));
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        if(button.id == 100) {
            minecraft.setScreen(parent);
        } else if(button.id == 101) {
            page = !page;
            pageButton.text = page ? ProtocolVersion.Type.ALPHA.label : ProtocolVersion.Type.BETA.label;
            for(int i = 0; i < versions.size(); i++) {
                ButtonWidget b = (ButtonWidget)buttons.get(i);
                b.active = !b.active;
                b.visible = !b.visible;
            }
        } else if(button.id <= versions.size()) {
            ProtocolVersion.setCurrentVersion(versions.get(button.id));
            minecraft.setScreen(parent);
        }
    }

    public void render(int x, int y, float delta) {
        renderBackground();
        drawCenteredTextWithShadow(textRenderer, I18n.getTranslation("multiproto.screen.changeVersion"),
                width / 2, 20, 16777215);
        super.render(x, y, delta);
    }

    public TextRenderer getTextRenderer() {
        return textRenderer;
    }
}
