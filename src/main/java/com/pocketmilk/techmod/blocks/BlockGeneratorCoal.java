package com.pocketmilk.techmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.pocketmilk.techmod.entities.TileGenerator;
import com.pocketmilk.techmod.items.PocketItems;

import net.minecraft.block.state.BlockStateContainer;



public class BlockGeneratorCoal extends BaseMachine {
	
	public BlockGeneratorCoal() {
		// Passes the desired block name to the BaseMachine, and what GUI name will be attached to it
		//     Block name             GUI Name
		super("BlockGeneratorCoal", "FurnaceGen");
		
		// By default, the generator will face North and won't be active. Can add the isBurning state as well later
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH)
				.withProperty(ACTIVE, false));
	}
	
	public void addRecipe() {
		GameRegistry.addRecipe(new ItemStack(PocketBlocks.blockGeneratorCoal),
				"AAA",
				"AAA",
				"AAA",
				'A', PocketItems.bubbaItem
		);
	}
	
	// When this block is placed in the world, it will create an instance of the Generator tile entity on the same position.
	@Override
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileGenerator();
	}
	
    // Gets the current state of the block and entity?
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
    	boolean burning = false;
    	if (world.getTileEntity(pos) instanceof TileGenerator) {
    		burning = ((TileGenerator) world.getTileEntity(pos)).isBurning();
    	}
        return state.withProperty(FACING, state.getValue(FACING)).withProperty(BlockGeneratorCoal.ACTIVE, burning);
    }
}