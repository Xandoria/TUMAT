package de.canitzp.tumat;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public class Config{

    public static boolean serverControl;
    public static boolean shouldRenderOverlay;
    public static float distanceToRenderSurvival, distanceToRenderCreative;
    public static boolean showEntityItems;
    public static boolean showEnergy;
    public static boolean showSpecialAbilities;
    public static float x, y, scale, toSaveX, toSaveY, toSaveScale;
    public static boolean showHarvestTooltip;

    public static boolean markDirty;

    public static Configuration config;

    public static void init(FMLPreInitializationEvent event){
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        init();
        config.save();
    }

    public static void init(){
        String cat = "general";
        serverControl = config.getBoolean("ServerControl", cat, false, "Should the Server control the tooltips for the clients. At the client this config changes nothing.");
        shouldRenderOverlay = config.getBoolean("ShouldRenderOverlay", cat, true, "The main on or off switch. Set this to false to deactivate the rendering.");
        cat = "distances";
        distanceToRenderSurvival = config.getFloat("DistanceOfViewSurvival", cat, 4.5F, 0F, 192F, "The distance a player in survival can see the blocks. Can be increased up to 12 chunks (192 blocks).");
        distanceToRenderCreative = config.getFloat("DistanceOfViewCreative", cat, 5F, 0F, 192F, "The distance a player in creative can see the blocks. Can be increased up to 12 chunks (192 blocks).");
        cat = "rendering";
        showEntityItems = config.getBoolean("ShowEntityItem", cat, true, "Should be the tooltip be activated for EntityItems");
        showEnergy = config.getBoolean("ShowEnergy", cat, true, "Should the tooltip shows the energy of the block or item");
        showSpecialAbilities = config.getBoolean("ShowSpecialAbilities", cat, false, "Should the tooltip shows special information about the block or item.");
        showHarvestTooltip = config.getBoolean("ShowHarvestability", cat, false, "Should the tooltip shows the harvest tool for the block you're looking at.");
        x = config.getFloat("XOffset", cat, 0, -4000, 4000, "The x offset for the tooltip. Can be changed via the ingame Gui");
        y = config.getFloat("YOffset", cat, 5, -4000, 4000, "The y offset for the tooltip. Can be changed via the ingame Gui");
        scale = config.getFloat("Scale", cat, 1.0F, 0F, 5F, "The scale factor for the tooltip. Can be changed via the ingame Gui");

        markDirtyCheck();
    }

    private static void markDirtyCheck(){
        if(markDirty){
            x = toSaveX;
            y = toSaveY;
            scale = toSaveScale;
        }
    }

    @SideOnly(Side.CLIENT)
    public static void loadConfigFromServer(NBTTagCompound nbt){
        if(nbt != null){
            shouldRenderOverlay = nbt.getBoolean("shouldRenderOverlay");
            distanceToRenderSurvival = nbt.getFloat("distanceToRenderSurvival");
            distanceToRenderCreative = nbt.getFloat("distanceToRenderCreative");
            showEntityItems = nbt.getBoolean("showEntityItems");
            showEnergy = nbt.getBoolean("showEnergy");
            showSpecialAbilities = nbt.getBoolean("showSpecialAbilities");
            showHarvestTooltip = nbt.getBoolean("showHarvestTooltip");
        }
    }

    public static NBTTagCompound sendConfigToClient(){
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("shouldRenderOverlay", shouldRenderOverlay);
        nbt.setFloat("distanceToRenderSurvival", distanceToRenderSurvival);
        nbt.setFloat("distanceToRenderCreative", distanceToRenderCreative);
        nbt.setBoolean("showEntityItems", showEntityItems);
        nbt.setBoolean("showEnergy", showEnergy);
        nbt.setBoolean("showSpecialAbilities", showSpecialAbilities);
        nbt.setBoolean("showHarvestTooltip", showHarvestTooltip);
        return nbt;
    }

}
