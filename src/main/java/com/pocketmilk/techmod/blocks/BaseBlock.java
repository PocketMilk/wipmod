package com.pocketmilk.techmod.blocks;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BaseBlock extends Block {
	
	public BaseBlock(String unlocalizedName, Material material, float hardness, float resistance) {
		super(material);
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setUnlocalizedName(unlocalizedName);
		this.setHardness(hardness);
		this.setResistance(resistance);
		//GUIid = ProgressiveAutomation.proxy.registerGui(machineType);
	}

}
