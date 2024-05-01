package com.github.zr0n1.multiproto.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import pl.telvarost.mojangfixstationapi.client.gui.CallbackButtonWidget;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.ServerData;

public class ChangeVersionScreen extends Screen {
    private Screen parent;
    private ServerData serverData;

    public ChangeVersionScreen(Screen parent, ServerData data) {
        this.parent = parent;
        this.serverData = data;
    }
    public ChangeVersionScreen(Screen parent) {
        this(parent, null);
    }

    public void init() {
        buttons.add(new CallbackButtonWidget(width / 2 - 100, height / 4 + 120 + 12,
            I18n.getTranslation("gui.cancel"), (button) -> {
                minecraft.setScreen(parent);
        }));
    }

    public void render(int mouseX, int mouseY, float delta) {
        renderBackground();
        drawCenteredTextWithShadow(textRenderer, I18n.getTranslation("multiplayer.viababric:changeVersion"),
        this.width / 2, 20, 16777215);
        super.render(mouseX, mouseY, delta);
    }
}
