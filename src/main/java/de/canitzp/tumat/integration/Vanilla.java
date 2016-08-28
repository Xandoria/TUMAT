package de.canitzp.tumat.integration;

import de.canitzp.tumat.Config;
import de.canitzp.tumat.InfoUtil;
import de.canitzp.tumat.api.IWorldRenderer;
import de.canitzp.tumat.api.TooltipComponent;
import de.canitzp.tumat.api.components.TextComponent;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Items;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class Vanilla implements IWorldRenderer{

    private static String[] vanillaHarvestLevel = {"Wood", "Stone", "Iron", "Diamond"};
    private static String[] tinkerHarvestLevel = {"Stone", "Iron", "Diamond", "Obsidian", "Cobalt"};
    public static boolean isTinkersConstructLoaded;

    @Override
    public TooltipComponent renderBlock(WorldClient world, EntityPlayerSP player, BlockPos pos, EnumFacing side, TooltipComponent component, boolean shouldCalculate){
        IBlockState state = world.getBlockState(pos);
        //Plants:
        if(state.getBlock() instanceof IPlantable){
            IBlockState plant = ((IPlantable) state.getBlock()).getPlant(world, pos);
            if(plant != null){
                component.clear();
                component.addOneLineRenderer(new TextComponent(InfoUtil.getBlockName(plant)));
                component.setModName(InfoUtil.getModNameFromBlock(plant.getBlock()));
            }
        }

        //Harvest Level
        if(Config.showHarvestTooltip){
            String level = getHarvestLevel(state.getBlock().getHarvestLevel(state));
            String tool = getHarvestTool(state);
            if(tool != null){
                String color = getTextColorTool(tool, player.getHeldItemMainhand());
                component.addOneLineRenderer(new TextComponent("Effective Tool: " + color + StringUtils.capitalize(tool)));
                if(level != null && color.equals(TextFormatting.GREEN.toString())){
                    color = getTextColorLevel(state, player.getHeldItemMainhand());
                    component.addOneLineRenderer(new TextComponent("Harvest Level: " + color + level));
                }
            }
        }

        if(state.getBlock() instanceof BlockRedstoneWire) {
            component.clear();
            component.addOneLineRenderer(new TextComponent(InfoUtil.getBlockName(state)));
            if(Config.showSpecialAbilities)
                component.addOneLineRenderer(new TextComponent("Power: " + state.getValue(BlockRedstoneWire.POWER)));
            component.setModName(InfoUtil.getModNameFromBlock(state.getBlock()));
        }

        return component;
    }

    @Nullable
    public static String getHarvestLevel(int harvestLevel){
        if(harvestLevel < 0){
            return null;
        }
        String[] tools = isTinkersConstructLoaded ? tinkerHarvestLevel : vanillaHarvestLevel;
        if(tools.length > harvestLevel){
            return tools[harvestLevel];
        }
        return "Unknown";
    }

    @Nullable
    public static String getHarvestTool(IBlockState state){
        if(state.getBlock() instanceof IShearable){
            return "shears";
        }
        return state.getBlock().getHarvestTool(state);
    }

    public static String getTextColorTool(String toolType, ItemStack activeStack){
        if(toolType.equals("shears")){
            return getColor(activeStack.getItem() instanceof ItemShears);
        }
        return getColor(activeStack.getItem().getToolClasses(activeStack).contains(toolType));
    }

    public static String getTextColorLevel(IBlockState state, ItemStack stack){
        return getColor(stack.getItem().canHarvestBlock(state) || stack.getItem().canHarvestBlock(state, stack));
    }

    private static String getColor(boolean b){
        return b ? TextFormatting.GREEN.toString() : TextFormatting.RED.toString();
    }

}