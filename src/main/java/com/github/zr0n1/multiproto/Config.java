package com.github.zr0n1.multiproto;

import blue.endless.jankson.Comment;
import blue.endless.jankson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.api.gcapi.api.ConfigName;
import net.glasslauncher.mods.api.gcapi.api.PreConfigSavedListener;
import net.glasslauncher.mods.api.gcapi.impl.EventStorage;
import net.minecraft.client.Minecraft;

public class Config implements PreConfigSavedListener {

    @ConfigName("Show version name")
    @Comment("Shows version name on in-game HUD")
    public Boolean showVersion = true;

    @ConfigName("Texture parity")
    @Comment("Changes textures to match chosen version")
    public Boolean textureParity = true;

    @ConfigName("Lighting parity")
    @Comment("Toggles smooth lighting to match chosen version")
    public Boolean lightingParity = true;

    @ConfigName("Name rendering parity")
    @Comment("Renders player names larger < Beta 1.3")
    public Boolean nameRenderParity = true;

    @ConfigName("Block item rendering parity")
    @Comment("Renders block items without color multipliers < Beta 1.6")
    public Boolean blockItemRenderParity = true;

    @ConfigName("Custom version name")
    @Comment("Custom version name on in-game HUD")
    public String customVersionName = "";


    @Override
    public void onPreConfigSaved(int source, JsonObject jsonA, JsonObject jsonB) {
        boolean textureParityA = jsonA.getBoolean("textureParity", true);
        boolean textureParityB = jsonB.getBoolean("textureParity", false);
        boolean lightingParityA = jsonA.getBoolean("lightingParity", true);
        boolean lightingParityB = jsonB.getBoolean("lightingParity", false);
        if (source == EventStorage.EventSource.USER_SAVE) {
            Minecraft mc = (Minecraft) FabricLoader.getInstance().getGameInstance();
            if (textureParityA != textureParityB) {
                textureParity = textureParityB;
                mc.textureManager.method_1096();
            }
            if (lightingParityA != lightingParityB) {
                lightingParity = lightingParityB;
                mc.worldRenderer.method_1537();
            }
        }
    }
}
