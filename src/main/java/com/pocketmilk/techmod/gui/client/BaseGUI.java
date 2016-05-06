package com.pocketmilk.techmod.gui.client;

import org.lwjgl.opengl.GL11;

import com.pocketmilk.techmod.ref.Ref;
//import com.vanhal.progressiveautomation.util.StringHelper;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

public class BaseGUI extends GuiContainer {
	public static int BLACK = 0x000000;
	public static int GRAY = 0x404040;
	public static int WHITE = 0xFFFFFF;
	public static int GREEN = 0x5CA028;
	public static int RED = 0xCC3333;
	public static int BLUE = 0x4C8BFF;
	public static int ORANGE = 0xFF9900;
	public int mouseX = 0;
	public int mouseY=0;
	
	protected int guiHeight = 166;
	protected int guiWidth = 176;
	
	public static final FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;
	
	// Define any extra images you want to draw in the GUI like this flame one
	public static final ResourceLocation flame = new ResourceLocation(Ref.MODID, "textures/gui/flame.png");
	
	protected ResourceLocation background;

	public BaseGUI(Container container, ResourceLocation texture) {
		super(container);
		background = texture;
	}
	
	
	// This function is what should be used to draw any text or tool-tip text
	// For per-block strings, override the drawText function
	// You can use the drawString functions defined below for basic text or use
	// drawHoveringText for a tool-tip like one ( drawHoveringText(List<String>, int x, int y)
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		drawText();
		drawString("Inventory", 8, guiHeight - 92, GRAY);
	}
	
	
	// This is used to grab the mouse X/Y to be used to determine if the mouse
	// is over a certain area and draw a tooltip for it inside of drawGuidrawGuiContainerForegroundLayer
	// using a drawHoveringText call
	public void drawScreen(int par1, int par2, float par3)
	{
		this.mouseX = par1;
		this.mouseY = par2;
		super.drawScreen(par1, par2, par3);
	}
	
	public void setHeight(int height) {
		guiHeight = height;
	}
	
	public void setWidth(int width) {
		guiWidth = width;
	}
	
	protected void drawText() {	}
	
	protected void drawElements() {}

	// This draws the main background image, plus any extra layers that the extension of this block wants to draw
	// via an overridden drawElements
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, guiWidth, guiHeight);
		drawElements();
	}
	
	protected void drawString(String text, int y, int colour) {
		drawString(text, ((xSize - fontRendererObj.getStringWidth(text)) / 2) , y, colour);
	}
	
	protected void drawString(String text, int x, int y, int colour) {
		fontRendererObj.drawString(text, x, y, colour);
	}
	
	protected void drawString(String text, int x, int w, int y, int colour) {
		drawString(text, x + ((w - fontRendererObj.getStringWidth(text)) / 2) , y, colour);
	}
	
	// This is how you would define a 'global' function to draw a certain area of the main texture image
	// This specific one draws part of the flame in the bottom right of the furnace image based on the burning level
	//public void drawFlame(float progress, int x, int y) {
	//	int level = (int)Math.ceil(16*progress);
	//	drawTexturedModalRect(guiLeft + x, guiTop + y + 16 - level, 240, 240 + (16-level), 16, level);
	//}

}
