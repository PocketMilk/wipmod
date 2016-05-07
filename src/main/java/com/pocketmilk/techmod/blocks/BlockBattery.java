package com.pocketmilk.techmod.blocks;

import com.pocketmilk.techmod.entities.TileBattery;
import com.pocketmilk.techmod.entities.TileGenerator;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBattery extends BaseMachine {

	public BlockBattery() {
		super("BlockBattery", "PowerStorage");
		
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH)
				.withProperty(ACTIVE, false));
	}

	@Override
	public void addRecipe() {
		
	}

	// When this block is placed in the world, it will create an instance of the Generator tile entity on the same position.
	@Override
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileBattery();
	}
	
    @Override
    public EnumBlockRenderType getRenderType (IBlockState state) {
        
        return EnumBlockRenderType.MODEL;
    }

	@Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
    	boolean hasPower = false;
    	boolean percent25 = false;
    	boolean percent50 = false;
    	boolean percent75 = false;
    	boolean percent100 = false;
    	
    	if (world.getTileEntity(pos) instanceof TileBattery) {
    		hasPower = ((TileBattery) world.getTileEntity(pos)).hasPower();
    		//percent25 = ((TileBattery) world.getTile)
    	}
        return state.withProperty(FACING, state.getValue(FACING)).withProperty(BlockBattery.ACTIVE, hasPower);
    }
}
