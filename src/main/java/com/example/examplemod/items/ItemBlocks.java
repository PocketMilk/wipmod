package com.example.examplemod.items;

import java.util.List;

import com.example.examplemod.blocks.BlockKappa;
//import com.example.examplemod.entities.BaseTileEntity;

import net.minecraft.block.Block;
//import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
//import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlocks extends ItemBlock {

	public ItemBlocks(Block baseBlock) {
		super(baseBlock);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par) {
		if (this.block instanceof BlockKappa) {
			list.add(TextFormatting.GRAY + "Used to fuck yourself silly.");
			list.add(TextFormatting.GRAY + "Just kidding.");
			list.add(TextFormatting.GRAY + "Lmao.");
		}
		
		if ( (itemStack != null) && (itemStack.getTagCompound() != null) ) {
			list.add(TextFormatting.GRAY + "Pre-Configured");
		}
		
    }
	
	/*@Override
	public boolean placeBlockAt(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {	
		boolean result = super.placeBlockAt(itemStack, player, world, pos, side, hitX, hitY, hitZ, newState);
		if (result) {
			if (!world.isRemote) {
				if (world.getTileEntity(pos) instanceof BaseTileEntity) {
					BaseTileEntity tileEntity = (BaseTileEntity) world.getTileEntity(pos);
					tileEntity.readFromItemStack(itemStack);
				}
			}
		}
		return result;
    }*/

}
