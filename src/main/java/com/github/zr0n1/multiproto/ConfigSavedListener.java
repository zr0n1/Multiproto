package com.github.zr0n1.multiproto;

import com.github.zr0n1.multiproto.parity.optional.TranslationParityHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.gcapi3.api.PreConfigSavedListener;
import net.glasslauncher.mods.gcapi3.impl.EventStorage;
import net.glasslauncher.mods.gcapi3.impl.GlassYamlFile;
import net.minecraft.client.Minecraft;

public class ConfigSavedListener implements PreConfigSavedListener {
    @Override
    public void onPreConfigSaved(int source, GlassYamlFile oldValues, GlassYamlFile newValues) {
        boolean textureParityA = oldValues.getBoolean("textureParity", true);
        boolean textureParityB = newValues.getBoolean("textureParity", false);
        boolean lightingParityA = oldValues.getBoolean("lightingParity", true);
        boolean lightingParityB = newValues.getBoolean("lightingParity", false);
        boolean translationParityA = oldValues.getBoolean("translationParity", true);
        boolean translationParityB = newValues.getBoolean("translationParity", false);
        if (source == EventStorage.EventSource.USER_SAVE) {
            Minecraft mc = (Minecraft) FabricLoader.getInstance().getGameInstance();
            if (textureParityA != textureParityB) {
                Multiproto.config.textureParity = textureParityB;
                mc.textureManager.reload();
            }
            if (lightingParityA != lightingParityB && mc.isWorldRemote()) {
                Multiproto.config.lightingParity = lightingParityB;
                mc.worldRenderer.reload();
            }
            if (translationParityA != translationParityB) {
                Multiproto.config.translationParity = translationParityB;
                TranslationParityHelper.applyParity();
            }
        }
    }
}
