package com.pocketmilk.techmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.pocketmilk.techmod.TechMod;
import com.pocketmilk.techmod.entities.TileGenerator;
import com.pocketmilk.techmod.items.PocketItems;
import com.pocketmilk.techmod.gui.SimpleGuiHandler;




public class BlockGeneratorCoal extends BaseMachine {
	private boolean keepInventory = false;
	
	public BlockGeneratorCoal() {
		// Passes the desired block name to the BaseMachine, and what GUI name will be attached to it
		//     Block name             GUI Name
		super("BlockGeneratorCoal");
		
		// By default, the generator will face North and won't be active. Can add the isBurning state as well later

	}
	
	public void addRecipe() {
		GameRegistry.addRecipe(new ItemStack(PocketBlocks.blockGeneratorCoal),
				"AAA",
				"AAA",
				"AAA",
				'A', PocketItems.bubbaItem
		);
	}
	
	// This function is called when the block is right-clicked, once the GUI is set up this will open it up
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			if (!(player instanceof FakePlayer)) {
				FMLNetworkHandler.openGui(player, TechMod.instance, SimpleGuiHandler.coalGeneratorID, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}
	
	// When this block is placed in the world, it will create an instance of the Generator tile entity on the same position.
	@Override
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileGenerator();
	}
	
	
    // Gets the current state of the block and entity?
	@Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
    	boolean burning = false;
    	if (world.getTileEntity(pos) instanceof TileGenerator) {
    		burning = ((TileGenerator) world.getTileEntity(pos)).isBurning();
    		System.out.println(burning);
    	}
        return state.withProperty(FACING, state.getValue(FACING)).withProperty(BlockGeneratorCoal.ACTIVE, burning);
    }
	
	public void setActive(Boolean active, World world, BlockPos pos)
	{
		EnumFacing facing = world.getBlockState(pos).getValue(FACING);
		IBlockState state = world.getBlockState(pos).withProperty(ACTIVE, active).withProperty(FACING, facing);
		world.setBlockState(pos, state, 3);
	}
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!keepInventory)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileGenerator)
            {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TileGenerator)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }
    
}