package com.github.zr0n1.multiproto;
import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

public class Config {

    @ConfigEntry(name="show protocol", description = "Show Version Protocol in F3 Debug Screen")
    public Boolean showProtocol = true;

    @ConfigEntry(name = "Version name parity", description = "Shows version name on HUD < Beta 1.6")
    public Boolean showVersion = true;

    @ConfigEntry(name = "\u200BTexture parity", description = "Changes textures to match version")
    public Boolean textureParity = true;

    @ConfigEntry(name = "\u200B\u200BLighting parity", description = "Toggles smooth lighting to match version")
    public Boolean lightingParity = true;

    @ConfigEntry(name = "\u200B\u200B\u200BName rendering parity", description = "enders player names larger < Beta 1.3")
    public Boolean nameRenderParity = true;

    @ConfigEntry(name = "\u200B\u200B\u200B\u200BTooltip name parity", description = "Changes tooltip names to match version")
    public Boolean translationParity = true;

    @ConfigEntry(name = "\u200B\u200B\u200B\u200B\u200BCustom version name", description = "Shows custom version name on HUD")
    public String customVersionName = "";

    @ConfigEntry(name = "Server side sound (UB Protocol Only)", description = "Make client play sound based on what server say")
    public Boolean play62Sound = false;

    @ConfigEntry(name = "Server side block breaking (UB Protocol Only)", description = "Visible Block breaking from other player")
    public Boolean blockBreaking = false;
}
