package com.pocketmilk.techmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BaseMachine extends BlockContainer {
	
	// machineType is used to connect an instance of this block to a specific GUI
	// This stores what direction the generator is facing
	public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	
	public BaseMachine(String unlocalizedName) {
		// For now, all Machine blocks will be Iron though we can change this later by passing a variable to this constructor (Wood, Stone, Iron tiers?)
		super(Material.iron);
		// Adds any instance of this block to the "Misc" creative tab, can be changed later as well.
		this.setCreativeTab(CreativeTabs.tabMisc);
		// Part of the block registering process
		this.setUnlocalizedName(unlocalizedName);
		// Supposedly this is important to set
		this.isBlockContainer = true;
		
		//this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	public void preInit() {
		this.addRecipe();
	}
	
	public abstract void addRecipe();
	
	
	// There shouldn't be any entities for this block because it's a base class
	@Override
	public TileEntity createNewTileEntity(World world, int var2) {
		return null;
	}
	
	// If we don't override this function, the block will be invisible in-game
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	
	// Creates the facing and active property states
	//@Override
	//protected BlockStateContainer createBlockState() {
	//	FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	//	return new BlockStateContainer(this, FACING);
	//} 
    
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
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int facingInt = getSideFromEnum(state.getValue(FACING));
		return facingInt;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		int facingInt = meta;
		EnumFacing facing = getSideFromint(facingInt);
		return this.getDefaultState().withProperty(FACING, facing);
	}
	
	public EnumFacing getSideFromint(int i)
	{
		if (i == 0)
		{
			return EnumFacing.NORTH;
		} else if (i == 1)
		{
			return EnumFacing.SOUTH;
		} else if (i == 2)
		{
			return EnumFacing.EAST;
		} else if (i == 3)
		{
			return EnumFacing.WEST;
		}
		return EnumFacing.NORTH;
	}
	
	public int getSideFromEnum(EnumFacing facing)
	{
		if (facing == EnumFacing.NORTH)
		{
			return 0;
		} 
		else if (facing == EnumFacing.SOUTH)
		{
			return 1;
		}
		else if (facing == EnumFacing.EAST)
		{
			return 2;
		} 
		else if (facing == EnumFacing.WEST)
		{
			return 3;
		}
		return 0;
	}
    
}