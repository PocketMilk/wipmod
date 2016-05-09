package com.pocketmilk.techmod.gui.client;

import org.lwjgl.opengl.GL11;

import com.pocketmilk.techmod.entities.TileGenerator;
import com.pocketmilk.techmod.gui.server.ContainerFurnaceGen;
import com.pocketmilk.techmod.ref.Ref;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

public class GUIFurnaceGen extends GuiContainer {
	public static final ResourceLocation texture = new ResourceLocation(Ref.MODID, "textures/gui/Generator.png");
	
	protected int guiHeight = 150;
	protected int guiWidth = 176;
	
	public int mouseX = 0;
	public int mouseY = 0;
	
	TileGenerator generator;
	ContainerFurnaceGen containerGenerator;
	
	public static final FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;

	public GUIFurnaceGen(EntityPlayer player, TileGenerator entity) {
		super(new ContainerFurnaceGen(entity, player));
		generator = entity;
		containerGenerator = (ContainerFurnaceGen) this.inventorySlots;
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		this.mouseX = par1;
		this.mouseY = par2;
		super.drawScreen(par1, par2, par3);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		// Background image of interface
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, guiWidth, guiHeight);
		
		// Power Bar
		
		//int percentAsWidth = (int)Math.ceil((generator.getPower()*160)/100);
		//System.out.println(generator.getPercentStorage());
		int energyLevel = Math.round((160*generator.getPercentStorage())/100);
		drawTexturedModalRect(guiLeft + 8, guiTop + 8, 8, 158, energyLevel, 12);
		
		// Flame
		if(generator.getBurnProgressPercent() != 0) {
		int level = Math.round((13*generator.getBurnProgressPercent())/100);
		drawTexturedModalRect(guiLeft + 48, guiTop + 36 + (12 - level), 241, 241 + (12-level), 14, level+1);
		}
		
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		
	}

	//protected void drawText() {
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
	//}
	
}
