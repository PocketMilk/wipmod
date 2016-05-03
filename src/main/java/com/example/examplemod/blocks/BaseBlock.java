package com.example.examplemod.blocks;

//import java.util.ArrayList;

import com.example.examplemod.ExampleMod;
//import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.example.examplemod.ref.Ref;
import com.example.examplemod.items.ItemBlocks;

//import cofh.api.block.IDismantleable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
//import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.init.Blocks;
//import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
//import net.minecraftforge.common.util.FakePlayer;
//import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
//import net.minecraftforge.oredict.ShapedOreRecipe;

public class BaseBlock extends BlockContainer {
	public String name;
	public int GUIid = -1;
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 0;
    }
	
	
	public BaseBlock() {
		super(Material.iron);
		name = "Kappa Block";
		setUnlocalizedName(name);
		
		
		setHardness(1.0f);
		
		//GUIid = ProgressiveAutomation.proxy.registerGui(machineType);
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		//if (!world.isRemote) {
		//	if (GUIid>=0) {
		//		if (!(player instanceof FakePlayer)) {
		//			FMLNetworkHandler.openGui(player, ProgressiveAutomation.instance, GUIid, world, pos.getX(), pos.getY(), pos.getZ());
		//		}
		//	}
		//}
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int var2) {
		return null;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		//BaseTileEntity tileEntity = (BaseTileEntity)world.getTileEntity(pos);
//
        //if (tileEntity != null) {
        //    ArrayList<ItemStack> items = getInsides(world, pos);
        //    
        //    for (ItemStack item: items) {
        //    	dumpItems(world, pos, item);
        //    }
            //world.func_147453_f(pos, state.getBlock()); //I have no idea what this method did....
        //}
        super.breakBlock(world, pos, state);
    }
	
	
	public void preInit() {
		GameRegistry.registerBlock(this, ItemBlocks.class, name);
	}
	
	public void init() {
		if (ExampleMod.proxy.isClient()) {
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.register(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Ref.MODID + ":" + name, "inventory"));
		}
	}
	
	public void postInit() {
	}
}
