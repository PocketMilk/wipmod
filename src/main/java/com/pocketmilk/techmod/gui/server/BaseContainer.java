package com.pocketmilk.techmod.gui.server;

import java.util.HashMap;

import com.pocketmilk.techmod.blocks.network.NetworkHandler;
import com.pocketmilk.techmod.blocks.network.PartialTileNBTUpdateMessage;
import com.pocketmilk.techmod.entities.BaseTile;
//import com.pocketmilk.techmod.gui.slots.SlotBurn;
//import com.pocketmilk.techmod.gui.slots.SlotPower;

import com.pocketmilk.techmod.gui.slots.SlotBurn;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public abstract class BaseContainer extends Container {
	public static final byte TICKS_PER_MESSAGE = 5;
	
	protected BaseTile entity;
	
	public HashMap<Integer, BaseSlot> slotMap = new HashMap<Integer, BaseSlot>();

	@Override
	protected Slot addSlotToContainer(Slot slotIn) {
		Slot slot = super.addSlotToContainer(slotIn);
		if(slot instanceof BaseSlot){
			//TODO remove player slots
			slotMap.put(slot.getSlotIndex(), (BaseSlot) slot);
		}
		return slot;
	}
	
	public static BaseContainer createContainer(Class<? extends BaseContainer> clazz, TileEntity tile, EntityPlayer player){
		try {
			BaseContainer container = clazz.newInstance();
			if(container instanceof IContainerLayout){
				((IContainerLayout) container).setPlayer(player);
				((IContainerLayout) container).setTile(tile);
				((IContainerLayout) container).addInventorySlots();
				((IContainerLayout) container).addPlayerSlots();
			}
			return container;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*public BaseContainer(BaseTile inEntity, int x, int y) {
		this(inEntity, x, y, "none");
	}*/
	
	public BaseContainer() {
	}

	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		//if (!entity.isLooked()) {
		//	entity.setLooked();
		//}
	}
	
	
}
