package com.drasticdemise.warpscrolls.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Bailey on 5/21/2016.
 */
public class ItemWarpScroll extends Item{

    public ItemWarpScroll() {
        setUnlocalizedName("itemWarpScroll");
        setRegistryName("itemWarpScroll");
        setMaxStackSize(1);
        setMaxDamage(1);
        setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.register(this);
    }

    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        if(!playerIn.isSneaking() && itemStackIn.getTagCompound().getIntArray("blockPos") != null){
            int[] positionArray = itemStackIn.getTagCompound().getIntArray("blockPos");
            try {
                playerIn.teleportTo_(positionArray[0], positionArray[1], positionArray[2]);
                itemStackIn.damageItem(1, playerIn);
                //Just because I am scared of servers crashing
                try {
                    playerIn.playSound(SoundEvent.soundEventRegistry.getObject(new ResourceLocation("entity.endermen.teleport")), 2, 1);
                }catch(Throwable t){}
            }catch(Exception e){
                System.out.println("Something went horribly wrong.");
            }
        }else if(playerIn.isSneaking()){
            //Get all of the positions and put them in an array
            int[] positionArray = new int[]{ (int)playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ};
            NBTTagCompound tag = new NBTTagCompound();
            tag.setIntArray("blockPos", positionArray);
            itemStackIn.setTagCompound(tag);
        }
        return new ActionResult(EnumActionResult.PASS, itemStackIn);
    }
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if(stack.getTagCompound() == null){
            stack.setTagCompound(new NBTTagCompound());
        }
        if(stack.getMaxDamage() - stack.getItemDamage() == 1){
            stack.damageItem(1, (EntityLivingBase)entityIn);
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        tooltip.add("No location set.");
        try {
            if(stack.getTagCompound() == null){
                return;
            }else {
                int[] pos = stack.getTagCompound().getIntArray("blockPos");
                if (pos != null) {
                    tooltip.set(1, "X: " + pos[0] + " Y: " + pos[1] + " Z: " + pos[2]);
                }
            }
        } catch (Exception e) {

        }
    }
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
