package com.pocketmilk.techmod.blocks;

import com.pocketmilk.techmod.TechMod;
import com.pocketmilk.techmod.entities.TileGenerator;

import java.util.ArrayList;
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

public abstract class BaseMachine extends BlockContainer {
	
	// machineType is used to connect an instance of this block to a specific GUI
	public String machineType;
	
	// GUIid will be set to an available number which can be used to make sure the GUI exists
	public int GUIid = -1;
	
	// This stores what direction the generator is facing
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	// This stores if the block is active or not (just for sending out energy apart from adding a isBurning bool?)
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
	
	
	public BaseMachine(String unlocalizedName, String machineType) {
		// For now, all Machine blocks will be Iron though we can change this later by passing a variable to this constructor (Wood, Stone, Iron tiers?)
		super(Material.iron);
		// Adds any instance of this block to the "Misc" creative tab, can be changed later as well.
		this.setCreativeTab(CreativeTabs.tabMisc);
		// Part of the block registering process
		this.setUnlocalizedName(unlocalizedName);
		// Setting local variable for machine type
		this.machineType = machineType;
		// Supposedly this is important to set
		this.isBlockContainer = true;
		
		// This will register a GUI to this instance of this block, for now we don't have that system set up
		GUIid = TechMod.proxy.registerGui(machineType);
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
	
	
	// This function is called when the block is right-clicked, once the GUI is set up this will open it up
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			if (GUIid>=0) {
				if (!(player instanceof FakePlayer)) {
					FMLNetworkHandler.openGui(player, TechMod.instance, GUIid, world, pos.getX(), pos.getY(), pos.getZ());
				}
			}
		}
		return true;
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
   
    
    public abstract IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos);
}