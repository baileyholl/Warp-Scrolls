package com.drasticdemise.warpscrolls;

import com.drasticdemise.warpscrolls.item.ItemWarpScroll;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Bailey on 5/21/2016.
 */
public class InitItems {
    public static ItemWarpScroll itemWarpScroll;

    public static void init(){
        itemWarpScroll = new ItemWarpScroll();
    }

    public static void recipes(){
        ItemStack paperStack = new ItemStack(Items.PAPER);
        ItemStack pearlStack = new ItemStack(Items.ENDER_PEARL);
        ItemStack warpStack = new ItemStack(InitItems.itemWarpScroll);
        GameRegistry.addRecipe(new ItemStack(InitItems.itemWarpScroll),
                "xxx",
                "xyx",
                "xxx",
                'x', paperStack, 'y', pearlStack);
    }

    public static void initModels(){
        itemWarpScroll.initModel();
    }
}
