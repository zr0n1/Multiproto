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
    @ConfigName("Visual parity")
    @Comment("Changes game visuals to match chosen version")
    public Boolean visualParity = true;

    @Override
    public void onPreConfigSaved(int source, JsonObject oldJson, JsonObject newJson) {
        boolean visualParityOld = oldJson.getBoolean("visualParity", true);
        boolean visualParityNew = newJson.getBoolean("visualParity", false);
        if(source == EventStorage.EventSource.USER_SAVE && visualParityOld != visualParityNew) {
            visualParity = visualParityNew;
            ((Minecraft)FabricLoader.getInstance().getGameInstance()).textureManager.method_1096();
        }
    }
}
