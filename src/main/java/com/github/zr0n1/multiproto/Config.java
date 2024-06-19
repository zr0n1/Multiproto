package com.github.zr0n1.multiproto;

import blue.endless.jankson.Comment;
import blue.endless.jankson.JsonObject;
import com.github.zr0n1.multiproto.parity.optional.TranslationHelper;
import net.glasslauncher.mods.api.gcapi.api.ConfigName;
import net.glasslauncher.mods.api.gcapi.api.PreConfigSavedListener;
import net.glasslauncher.mods.api.gcapi.impl.EventStorage;
import net.minecraft.client.Minecraft;

import static com.github.zr0n1.multiproto.util.UtilKt.getMinecraft;

public class Config implements PreConfigSavedListener {

    @ConfigName("Version name parity")
    @Comment("Shows version name on HUD < Beta 1.6")
    public Boolean showVersion = true;

    @ConfigName("\u200BTexture parity")
    @Comment("Changes textures to match version")
    public Boolean textureParity = true;

    @ConfigName("\u200B\u200BLighting parity")
    @Comment("Toggles smooth lighting to match version")
    public Boolean lightingParity = true;

    @ConfigName("\u200B\u200B\u200BName rendering parity")
    @Comment("Renders player names larger < Beta 1.3")
    public Boolean nameRenderParity = true;

    @ConfigName("\u200B\u200B\u200B\u200BTooltip name parity")
    @Comment("Changes tooltip names to match version")
    public Boolean translationParity = true;

    @ConfigName("\u200B\u200B\u200B\u200B\u200BDebug HUD version")
    @Comment("Shows protocol version on debug HUD")
    public Boolean showDebug = false;

    @ConfigName("\u200B\u200B\u200B\u200B\u200B\u200BCustom version name")
    @Comment("Shows custom version name on HUD")
    public String customVersionName = "";


    @Override
    public void onPreConfigSaved(int source, JsonObject jsonA, JsonObject jsonB) {
        boolean textureParityA = jsonA.getBoolean("textureParity", true);
        boolean textureParityB = jsonB.getBoolean("textureParity", false);
        boolean lightingParityA = jsonA.getBoolean("lightingParity", true);
        boolean lightingParityB = jsonB.getBoolean("lightingParity", false);
        boolean translationParityA = jsonA.getBoolean("translationParity", true);
        boolean translationParityB = jsonB.getBoolean("translationParity", false);
        if (source == EventStorage.EventSource.USER_SAVE) {
            Minecraft mc = getMinecraft();
            if (textureParityA != textureParityB) {
                textureParity = textureParityB;
                mc.textureManager.method_1096();
            }
            if (lightingParityA != lightingParityB && mc.isWorldRemote()) {
                lightingParity = lightingParityB;
                mc.worldRenderer.method_1537();
            }
            if (translationParityA != translationParityB) {
                translationParity = translationParityB;
                TranslationHelper.INSTANCE.invoke();
            }
        }
    }
}