package com.pocketmilk.techmod.gui.client;

import com.pocketmilk.techmod.entities.TileGenerator;
import com.pocketmilk.techmod.gui.server.ContainerFurnaceGen;
import com.pocketmilk.techmod.ref.Ref;
//import com.pocketmilk.techmod.util.StringHelper;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GUIFurnaceGen extends BaseGUI {
	public static final ResourceLocation texture = new ResourceLocation(Ref.MODID, "textures/gui/Generator.png");
	
	protected int infoScreenX = 68;
	protected int infoScreenW = 87;
	protected int infroScreenY1 = 16;
	protected int infroScreenY2 = 26;
	protected int infroScreenY3 = 36;
	
	TileGenerator generator;

	public GUIFurnaceGen(InventoryPlayer inv, TileEntity entity) {
		super(new ContainerFurnaceGen(inv, entity), texture);
		generator = (TileGenerator)entity;
		this.setHeight(135);
	}

	protected void drawText() {
		/*
		drawString(StringHelper.localize("gui.generator"), 5, GRAY);
		drawString(StringHelper.getScaledNumber(generator.getEnergyStored(), 100) + " / " + StringHelper.getScaledNumber(generator.getMaxEnergyStored()) + " RF", infoScreenX, infoScreenW, infroScreenY3, RED);
		
		boolean ready = false;
		if ( (!generator.hasFuel()) && (!generator.isBurning()) ) {
			drawString(StringHelper.localize("gui.need.fuel"), infoScreenX, infoScreenW, infroScreenY2, RED);
		} else if ( (generator.getEnergyStored()>=generator.getMaxEnergyStored()) && (!generator.isBurning()) ) {
			ready = true;
			drawString(StringHelper.localize("gui.full"), infoScreenX, infoScreenW, infroScreenY2, GREEN);
		} else {
			ready = true;
			drawString("+" + generator.getProduceRate()+ " RF/t", infoScreenX, infoScreenW, infroScreenY2, BLUE);
		}
		
		if (!ready) {
			drawString(StringHelper.localize("gui.notready"), infoScreenX, infoScreenW, infroScreenY1, RED);
		} else if (generator.isBurning()) {
			drawString(StringHelper.localize("gui.running"), infoScreenX, infoScreenW, infroScreenY1, BLUE);
		} else {
			drawString(StringHelper.localize("gui.waiting"), infoScreenX, infoScreenW, infroScreenY1, GREEN);
		}
		*/
	}
	
	protected void drawElements() {
		//drawFlame(generator.getPercentDone(), 45, 23);
	}
}
