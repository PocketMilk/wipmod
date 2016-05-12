package com.pocketmilk.techmod.blocks;

import com.pocketmilk.techmod.entities.TileBattery;
import com.pocketmilk.techmod.entities.TileGenerator;
import com.pocketmilk.techmod.entities.TileWire;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockWireBase extends BlockContainer {
	
	public static PropertyBool NORTH = PropertyBool.create("north");
	public static PropertyBool SOUTH = PropertyBool.create("south");
	public static PropertyBool EAST = PropertyBool.create("east");
	public static PropertyBool WEST = PropertyBool.create("west");
	public static PropertyBool UP = PropertyBool.create("up");
	public static PropertyBool DOWN = PropertyBool.create("down");
	
	public BlockWireBase() {
		super(Material.iron);
		this.setCreativeTab(CreativeTabs.tabMisc);
		// Part of the block registering process
		this.setUnlocalizedName("BlockWireBase");
		this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, false).withProperty(SOUTH, false).withProperty(EAST, false).withProperty(WEST, false).withProperty(UP, false).withProperty(DOWN, false));
	}
	
	public void preInit() {
		this.addRecipe();
	}
	
	
	@Override
	protected BlockStateContainer createBlockState() {
		NORTH = PropertyBool.create("north");
		SOUTH = PropertyBool.create("south");
		EAST = PropertyBool.create("east");
		WEST = PropertyBool.create("west");
		UP = PropertyBool.create("up");
		DOWN = PropertyBool.create("down");
		return new BlockStateContainer(this, NORTH, SOUTH, EAST, WEST, UP, DOWN);
	} 
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
		//System.out.println("Block at " + pos + "; Up: " + state.getValue(UP) + " and Down: " + state.getValue(DOWN));
		TileWire thisTile = (TileWire) worldIn.getTileEntity(pos);
		return this.getDefaultState().withProperty(NORTH, thisTile.connectedNorth).withProperty(SOUTH, thisTile.connectedSouth).withProperty(EAST, thisTile.connectedEast).withProperty(WEST, thisTile.connectedWest).withProperty(UP, thisTile.connectedUp).withProperty(DOWN, thisTile.connectedDown);
    }
	@Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	@Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }
	@Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }


	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			if (!(player instanceof FakePlayer)) {
				TileEntity tileentity = world.getTileEntity(pos);
				if (tileentity instanceof TileWire) {
					TileWire tWire = (TileWire)tileentity;
					System.out.println("Power in wire: " + tWire.getPower());
	            }
				//FMLNetworkHandler.openGui(player, TechMod.instance, SimpleGuiHandler.batteryID, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}
	
	public void addRecipe() {
		
	}
	

	// When this block is placed in the world, it will create an instance of the Generator tile entity on the same position.
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileWire();
		}
	
	@Override
    public EnumBlockRenderType getRenderType (IBlockState state) {
        
        return EnumBlockRenderType.MODEL;
    }

}
