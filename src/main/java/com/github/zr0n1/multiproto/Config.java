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
    @Comment("Show version name on in-game HUD")
    public Boolean showVersion = true;
    @ConfigName("Version graphics")
    @Comment("Automatically applies textures and graphics matching the multiplayer version")
    public Boolean versionGraphics = true;

    @Override
    public void onPreConfigSaved(int source, JsonObject oldJson, JsonObject newJson) {
        boolean versionGraphicsOld = oldJson.getBoolean("versionGraphics", true);
        boolean versionGraphicsNew = newJson.getBoolean("versionGraphics", false);
        if(source == EventStorage.EventSource.USER_SAVE && versionGraphicsOld != versionGraphicsNew) {
            versionGraphics = versionGraphicsNew;
            ((Minecraft)FabricLoader.getInstance().getGameInstance()).textureManager.method_1096();
        }
    }
}
