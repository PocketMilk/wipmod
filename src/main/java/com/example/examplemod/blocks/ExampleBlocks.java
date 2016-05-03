package com.example.examplemod.blocks;

import java.util.ArrayList;
import java.util.List;

import com.example.examplemod.blocks.BlockKappa;
import com.vanhal.progressiveautomation.blocks.BlockMiner;

import net.minecraft.block.Block;

public class ExampleBlocks {
	
	// Create list of blocks here
	public static void preInit() {
		kappawappa.add(new BlockKappa());
	}
	
	// Loop through blocks and call their init, block should register itself in it
	public static void init() {
		
	}
	
	// Loop through blocks and call their postInit (should do nothing anyways for now)
	public static void postInit() {
		
	}
	
	
}

public static List<BlockKappa> kappawappa = new ArrayList<BlockKappa>(1);