package com.baileyhollingsworth.warpscrolls.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
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
        setUnlocalizedName("item_warp_scroll");
        setRegistryName("item_warp_scroll");
        setMaxStackSize(1);
        setMaxDamage(1);
        setCreativeTab(CreativeTabs.COMBAT);
        GameRegistry.register(this);
    }
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemStackIn = playerIn.getHeldItem(handIn);
        if(!playerIn.isSneaking() && itemStackIn.getTagCompound().getIntArray("blockPos") != null){
            int[] positionArray = itemStackIn.getTagCompound().getIntArray("blockPos");
            try {
                if(itemStackIn.getTagCompound().getInteger("dim") == playerIn.dimension) {
                    playerIn.attemptTeleport(positionArray[0], positionArray[1], positionArray[2]);
                    itemStackIn.damageItem(1, playerIn);
                    //Just because I am scared of servers crashing
                    try {
                        playerIn.playSound(SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), 2, 1);
                    } catch (Throwable t) {
                    }
                }else{
                    playerIn.addChatComponentMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Attempting to teleport in a different dimension would be a bad idea."), true);
                }
            }catch(Exception e){
                System.out.println("Player tried to teleport with a bad scroll or a sound attempted to crash the server.");
            }
        }else if(playerIn.isSneaking()){
            //Pass an array of the positions to reparse later.
            NBTTagCompound tag = new NBTTagCompound();
            tag.setIntArray("blockPos", new int[]{ (int)playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ});
            tag.setInteger("dim", playerIn.dimension);
            itemStackIn.setTagCompound(tag);
            playerIn.addChatComponentMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Location has been set at X: " +
                    (int)playerIn.posX + " Y: " + (int)playerIn.posY + " Z: " + (int)playerIn.posZ), true);
        }
        return new ActionResult(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
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
        tooltip.add("No location set. Right click while sneaking to set location.");
        try {
            if(stack.getTagCompound() == null){
                return;
            }else {
                int[] pos = stack.getTagCompound().getIntArray("blockPos");
                if (pos != null) {
                    tooltip.set(1, "X: " + pos[0] + " Y: " + pos[1] + " Z: " + pos[2] + ". Right-click while sneaking to set again.");
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
