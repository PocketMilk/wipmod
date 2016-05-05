package com.pocketmilk.techmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.pocketmilk.techmod.entities.TileGenerator;

import net.minecraft.block.state.BlockStateContainer;



public class BlockGeneratorCoal extends BaseMachine {
	
	// This stores what direction the generator is facing
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	// This stores if the block is active or not (just for sending out energy apart from adding a isBurning bool?)
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
	
	public BlockGeneratorCoal() {
		// Passes the desired block name to the BaseMachine, and what GUI name will be attached to it
		//     Block name             GUI Name
		super("BlockGeneratorCoal", "FurnaceGen");
		
		// By default, the generator will face North and won't be active. Can add the isBurning state as well later
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH)
				.withProperty(ACTIVE, false));
	}
	
	// When this block is placed in the world, it will create an instance of the Generator tile entity on the same position.
	@Override
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileGenerator();
	}
	
	// idk yet
	@Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }
	
	//idk yet
	@Override
    public IBlockState getStateFromMeta(int meta) {
    	EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }
	
	// Creates the facing and active property states
	@Override
	protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, new IProperty[] {FACING, ACTIVE});
	} 
    
	// When this block is placed, the facing property is oriented towards yourself
    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }
    // When this block is placed by someone else, the facing property is oriented towards the placer
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing()), 2);
    }
    
    // I'm assuming this is called when the world is loaded, or it's placed by some other means
    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
           this.setDefaultFacing(world, pos, state);
    }
    private void setDefaultFacing(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			Block block0 = world.getBlockState(pos.north()).getBlock();
			Block block1 = world.getBlockState(pos.south()).getBlock();
			Block block2 = world.getBlockState(pos.east()).getBlock();
			Block block3 = world.getBlockState(pos.west()).getBlock();
			EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
			
			if (enumfacing == EnumFacing.NORTH && block0.isFullBlock(state) && !block1.isFullBlock(state)) {
				enumfacing = EnumFacing.SOUTH;
			}
			
			if (enumfacing == EnumFacing.SOUTH && block1.isFullBlock(state) && !block0.isFullBlock(state)) {
				enumfacing = EnumFacing.NORTH;
			}
			
			if (enumfacing == EnumFacing.WEST && block3.isFullBlock(state) && !block2.isFullBlock(state)) {
				enumfacing = EnumFacing.EAST;
			}
			
			if (enumfacing == EnumFacing.EAST && block2.isFullBlock(state) && !block3.isFullBlock(state)) {
				enumfacing = EnumFacing.WEST;
			}
			
			world.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
		}
    }
    
    // Gets the current state of the block and entity?
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
    	boolean buring = false;
    	if (world.getTileEntity(pos) instanceof TileGenerator) {
    		buring = ((TileGenerator) world.getTileEntity(pos)).isBurning();
    	}
        return state.withProperty(FACING, state.getValue(FACING)).withProperty(BlockGeneratorCoal.ACTIVE, buring);
    }
	
}