package com.pocketmilk.techmod.blocks;

import com.pocketmilk.techmod.TechMod;

import java.util.ArrayList;
import cofh.api.block.IDismantleable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public abstract class BaseMachine extends BlockContainer implements IDismantleable {
	
	// machineType is used to connect an instance of this block to a specific GUI
	public String machineType;
	
	// GUIid will be set to an available number which can be used to make sure the GUI exists
	public int GUIid = -1;
	
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
			System.out.println("Block activated!");
			if (GUIid>=0) {
				System.out.println("Has GUI ID of "+GUIid);
				if (!(player instanceof FakePlayer)) {
					System.out.println("Player is not a FakePlayer instance?");
					FMLNetworkHandler.openGui(player, TechMod.instance, GUIid, world, pos.getX(), pos.getY(), pos.getZ());
				}
			}
		}
		return true;
	}
	
	
	// Once we get the dismantling rules/tools set up, this function is called when a maachine block is dismantled (Currently does nothing anyways)
	@Override
	public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnDrops) {
		BlockPos pos = new BlockPos(x, y, z);
		
		Block targetBlock = world.getBlockState(pos).getBlock();
		ItemStack block = new ItemStack(targetBlock);

		// Get the NBT tag contents
		//if (world.getTileEntity(pos) instanceof BaseTileEntity) {
		//	BaseTileEntity tileEntity = ((BaseTileEntity) world.getTileEntity(pos));
		//	tileEntity.writeToItemStack(block);
		//}

		
		//if (!returnDrops) {
	    //    dumpItems(world, pos, block);
		//	// Remove the tile entity first, so inventory/upgrades doesn't get dumped
		//	world.removeTileEntity(pos);
		//	world.setBlockToAir(pos);
		//}

		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		items.add(block);
		return items;
	}
	
	
	// By default, the machine blocks can be dismantled. We can override this per block if we don't want it to be
	@Override
	public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z) {
		return true;
	}
}