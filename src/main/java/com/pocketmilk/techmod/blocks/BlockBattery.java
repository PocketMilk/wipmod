package com.pocketmilk.techmod.blocks;

import com.pocketmilk.techmod.TechMod;
import com.pocketmilk.techmod.entities.TileBattery;
import com.pocketmilk.techmod.gui.SimpleGuiHandler;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class BlockBattery extends BaseMachine {
	
	//public static PropertyInteger BUFFER = PropertyInteger.create("buffer", 0, 15);
	public static PropertyInteger PERCENT = PropertyInteger.create("percent", 0, 4);
	//public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public BlockBattery() {
		super("BlockBattery");
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(PERCENT, 0));
	}
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING, PERCENT});
	} 
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
		TileBattery thisTile = (TileBattery) worldIn.getTileEntity(pos);
		int tilePercent = thisTile.getPwrAmount();
		//System.out.println(tilePercent);
		return state.withProperty(FACING, state.getValue(FACING)).withProperty(PERCENT, tilePercent);
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
	
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			if (!(player instanceof FakePlayer)) {
				FMLNetworkHandler.openGui(player, TechMod.instance, SimpleGuiHandler.batteryID, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}
	

	public void addRecipe() {
		
	}
	

	// When this block is placed in the world, it will create an instance of the Generator tile entity on the same position.
	@Override
	public TileEntity createNewTileEntity(World world, int var2) {
		//System.out.println("On tile create " + this.blockState.getBaseState().getValue(PERCENT));
		return new TileBattery();
	}
	
    @Override
    public EnumBlockRenderType getRenderType (IBlockState state) {
        
        return EnumBlockRenderType.MODEL;
    }

}
