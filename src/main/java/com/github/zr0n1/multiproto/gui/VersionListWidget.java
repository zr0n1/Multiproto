package com.github.zr0n1.multiproto.gui;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widgets.ScrollableScreen;
import net.minecraft.client.render.Tessellator;

import java.util.Comparator;
import java.util.List;

public class VersionListWidget extends ScrollableScreen {
    private final ChangeVersionScreen parent;
    private List<ProtocolVersion> versions;
    private ProtocolVersion selected;

    public VersionListWidget(ChangeVersionScreen parent) {
        super((Minecraft)FabricLoader.getInstance().getGameInstance(),
                parent.width, parent.height, 32, parent.height - 64,36);
        this.parent = parent;
        this.versions = ProtocolVersion.PROTOCOL_VERSIONS.stream().sorted(Comparator.reverseOrder()).toList();
    }

    @Override
    protected int getSize() {
        return versions.size();
    }

    @Override
    protected void entryClicked(int i, boolean doubleClick) {
        selected = versions.get(i);
    }

    @Override
    protected boolean isEntrySelected(int i) {
        return selected == versions.get(i);
    }

    @Override
    protected void renderBackground() {
        parent.renderBackground();
    }

    @Override
    protected void renderEntry(int i, int x, int y, int l, Tessellator tesselator) {
        ProtocolVersion v = versions.get(i);
        Multiproto.LOGGER.info(v.names.range());
        parent.drawTextWithShadow(parent.getTextManager(), v.names.range(), x + 2, y + 1, 16777215);
        parent.drawTextWithShadow(this.parent.getTextManager(), v.names.range(true), x + 2, y + 12, 8421504);
    }
}
