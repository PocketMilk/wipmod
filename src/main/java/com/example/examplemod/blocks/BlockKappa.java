package com.example.examplemod.blocks;


import com.example.examplemod.blocks.BaseBlock;


import net.minecraft.block.Block;
//import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
//import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockAccess;
//import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockKappa extends BaseBlock {
	
	public BlockKappa() {
		
	}
	
	//public TileEntity createNewTileEntity(World world, int var2) {
	//	return ...var2;
	//}
	
	public void addRecipe(Block previousTier) {
		ShapedOreRecipe recipe = null;
		recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sss", "scs", "sss", 's', Blocks.stone, 'c', Blocks.iron_block});
		GameRegistry.addRecipe(recipe);
	}
}