package com.pocketmilk.techmod.blocks;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
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
	
	// This stores if the block is active or not (just for sending out energy apart from adding a isBurning bool?)
    public static PropertyBool ACTIVE = PropertyBool.create("active");
	
	public BlockGeneratorCoal() {
		// Passes the desired block name to the BaseMachine
		super("BlockGeneratorCoal");
		
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));

	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
		ACTIVE = PropertyBool.create("active");
		return new BlockStateContainer(this, FACING, ACTIVE);
	} 
	
	// This is where we define the crafting recipe for this block
	public void addRecipe() {
		GameRegistry.addRecipe(new ItemStack(PocketBlocks.blockGeneratorCoal),
				"AAA",
				"AAA",
				"AAA",
				'A', PocketItems.bubbaItem
		);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int facingInt = getSideFromEnum(state.getValue(FACING));
		int activeInt = state.getValue(ACTIVE) ? 4 : 0;
		//System.out.println(facingInt + " " + activeInt);
		return facingInt + activeInt;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		boolean active = false;
		int facingInt = meta;
		//System.out.println(meta);
		if (facingInt > 3)
		{
			active = true;
			facingInt = facingInt - 4;
		}
		//System.out.println(facingInt + " " + active);
		EnumFacing facing = getSideFromint(facingInt);
		return this.getDefaultState().withProperty(FACING, facing).withProperty(ACTIVE, active);
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
	
    // This block has a container, so it should drop all of the items inside of the fuel slot when broken
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