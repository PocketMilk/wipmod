package com.pocketmilk.techmod.entities;


import com.pocketmilk.techmod.blocks.network.PartialTileNBTUpdateMessage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;


// This file is the clusterfuck of overridden functions required by the cofh energy api
// Honestly, fuck explaining each one myself so if you have the API in the project directories they have explanations for each thing
public class BaseTile extends TileEntity implements ISidedInventory, IEnergyProvider, IEnergyReceiver, ITickable {
	protected ItemStack[] slots;
	protected boolean dirty;
	protected int progress = 0;
	
	public EnumFacing facing = EnumFacing.NORTH;
	
	private NBTTagCompound partialUpdateTag = new NBTTagCompound();
	
	public int SLOT_INVENTORY_START = -1;
	public int SLOT_INVENTORY_END = -1;
	
	public BaseTile() {
		super();
	}
	
	public BaseTile(int numSlots) {
		super();
		slots = new ItemStack[numSlots+1];
		if (numSlots > 9) {
			SLOT_INVENTORY_START = numSlots - 8;
			SLOT_INVENTORY_END = numSlots;
			//ProgressiveAutomation.logger.info("Start: "+SLOT_INVENTORY_START+" End: "+SLOT_INVENTORY_END);
		} else {
			SLOT_INVENTORY_START = SLOT_INVENTORY_END = numSlots;
		}
	}
	
	public boolean isBurning() {
		return (progress>0);
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	@Override
	public void update() {
	}
	
	
	//Energy handling functions
	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return 0;
	}
	
	public int getEnergyStored(EnumFacing from) {
		return 0;
	}
	
	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		return 0;
	}
	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		return 0;
	}
	
	public boolean canConnectEnergy(EnumFacing from) {
		 return true;
	}
	
	//Item handling functions
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		int[] output = new int[slots.length];
		for (int i=0; i<slots.length; i++) {
			output[i] = i;
		}
		return output;
	}
	
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing face) {
		//if (sides[face.ordinal()] == WrenchModes.Mode.Disabled) return false;
		//if ( (slot>=SLOT_INVENTORY_START) && (slot<=SLOT_INVENTORY_END) ) {
		//	if ( (sides[face.ordinal()] == WrenchModes.Mode.Normal) || (sides[face.ordinal()] == WrenchModes.Mode.Output) ) return true;
		//}
		return true;
	}
	
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing face) {
		if ( (slots[slot] != null) 
				&& (slots[slot].isItemEqual(stack))
				&& (ItemStack.areItemStackTagsEqual(stack, slots[slot])) ) {
			int availSpace = this.getInventoryStackLimit() - slots[slot].stackSize;
			if (availSpace>0) {
				return true;
			}
		} else if (isItemValidForSlot(slot, stack)) {
			return true;
		}
		return false;
	}
	
	@Override
	public int getSizeInventory() {
		return slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return slots[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		if (slots[slot] != null) {
			ItemStack newStack;
			if (slots[slot].stackSize <= amt) {
				newStack = slots[slot];
				slots[slot] = null;
			} else {
				newStack = slots[slot].splitStack(amt);
				if (slots[slot].stackSize == 0) {
					slots[slot] = null;
				}
			}
			return newStack;
		}
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int slot) {
		if (slots[slot]!=null) {
			ItemStack stack = slots[slot];
			slots[slot] = null;
			return stack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		slots[slot] = stack;
		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
	}
	
	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(getPos()) == this &&
		 player.getDistanceSq(getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5) < 64;
	}

	@Override
	public void openInventory(EntityPlayer playerIn) {}

	@Override
	public void closeInventory(EntityPlayer playerIn) {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;//this.isItemValidForSlot(slot, stack, false);
	}
	
	@Override
	public void clear() {
		for (int i = 0; i < this.slots.length; ++i) {
            this.slots[i] = null;
        }
	}
	
	// NBT DATA STUFF
	/**
	 * Do not extend this method, use writeSyncOnlyNBT, writeCommonNBT or writeNonSyncableNBT as needed.
	 */
	@Override
	public final void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		
		writeCommonNBT(nbt);
		writeNonSyncableNBT(nbt);
	}
	
	/**
	 * Do not extend this method, use readSyncOnlyNBT, readCommonNBT or readNonSyncableNBT as needed.
	 */
	@Override
	public final void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		
		readCommonNBT(nbt);
		readNonSyncableNBT(nbt);
	}

	public void readFromItemStack(ItemStack itemStack) {
		if (itemStack == null || itemStack.getTagCompound() == null) {
			return;
		}
		readCommonNBT(itemStack.getTagCompound());
		readNonSyncableNBT(itemStack.getTagCompound());
	}

	public void writeToItemStack(ItemStack itemStack) {
		if (itemStack == null ) {
			return;
		}
		if (itemStack.getTagCompound() == null) {
			itemStack.setTagCompound(new NBTTagCompound());
		}
		writeCommonNBT(itemStack.getTagCompound());
		writeNonSyncableNBT(itemStack.getTagCompound());
	}
	
	/**
	 * This overridable method is intended for writing to NBT all data that is used only at runtime
	 * and never saved to hdd.
	 * @param nbt
	 */
	protected void writeSyncOnlyNBT(NBTTagCompound nbt) {
	}
	
	/**
	 * This overridable method is intended for writing to NBT all data that is both saved to hdd
	 * and can be sent to the client TileEntity through S35PacketUpdateTileEntity.
	 * 
	 * WARNING: Do not include slot contents here. 
	 * They are automagically synced when a GUI is opened using S30PacketWindowItems 
	 * @param nbt
	 */
	public void writeCommonNBT(NBTTagCompound nbt) {
		//nbt.setInteger("Progress", progress);
		//nbt.setInteger("BurnLevel", burnLevel);
		//nbt.setBoolean("firstLook", firstLook);
		nbt.setInteger("facing", facing.ordinal());
		//int ary[] = new int[6];
		//for (int i = 0; i < 6; i++) {
		//	ary[i] = sides[i].ordinal();
		//}
		//nbt.setIntArray("sides", ary);
		//ary = null;
	}
	
	/**
	 * This overridable method is intended for writing to NBT all data that will 
	 * NOT be synced using S35PacketUpdateTileEntity packets.
	 * 
	 * This includes, but is not limited to, slot contents. 
	 * See writeSyncableNBT for more info.
	 * @param nbt
	 */
	public void writeNonSyncableNBT(NBTTagCompound nbt) {
		
		NBTTagList contents = new NBTTagList();
		for (int i = 0; i < slots.length; i++) {
			if (slots[i] != null) {
				ItemStack stack = slots[i];
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte)i);
				stack.writeToNBT(tag);
				contents.appendTag(tag);
			}
		}
		nbt.setTag("Contents", contents);
	}
	
	/**
	 * This overridable method is intended for reading from NBT all data that is used only at runtime
	 * and never saved to hdd.
	 * @param nbt
	 */
	public void readSyncOnlyNBT(NBTTagCompound nbt) {
	}
	
	/**
	 * This overridable method is intended for reading from NBT all data that is both saved to hdd
	 * and can be sent to the client TileEntity through S35PacketUpdateTileEntity and PartialTileNBTUpdateMessage.
	 * 
	 * WARNING: Please check if the tag exists before reading it. Due to the nature of 
	 * PartialTileNBTUpdateMessage properties that didn't change since the last message
	 * will not be included.
	 * 
	 * WARNING: Do not include slot contents here. 
	 * They are automagically synced when a GUI is opened using S30PacketWindowItems 
	 * @param nbt
	 */
	public void readCommonNBT(NBTTagCompound nbt) {
		
		//if (nbt.hasKey("Progress")) progress = nbt.getInteger("Progress");
		//if (nbt.hasKey("BurnLevel")) burnLevel = nbt.getInteger("BurnLevel");
		//if (nbt.hasKey("firstLook")) firstLook = nbt.getBoolean("firstLook");
		if (nbt.hasKey("facing")) facing = EnumFacing.getFront(nbt.getInteger("facing"));
		//if (nbt.hasKey("sides")) {
		//	int ary[] = nbt.getIntArray("sides");
		//	for (int i = 0; i<6; i++) {
		//		sides[i] = WrenchModes.modes.get(ary[i]);
		//	}
		//}
	}
	
	/**
	 * This overridable method is intended for reading from NBT all data that is only saved to hdd 
	 * and never synced between client and server, or is synced using a different method (e.g. inventory 
	 * contents and S30PacketWindowItems)
	 * @param nbt
	 */
	protected void readNonSyncableNBT(NBTTagCompound nbt) {
		
		NBTTagList contents = nbt.getTagList("Contents", 10);
		for (int i = 0; i < contents.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) contents.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot < slots.length) {
				slots[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
	}
	
    /**
     * This method is used to sync data when a GUI is opened. the packet will contain
     * all syncable data.
     */
	@Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeCommonNBT(nbttagcompound);
        this.writeSyncOnlyNBT(nbttagcompound);
        return new SPacketUpdateTileEntity(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ()), -1, nbttagcompound);
    }
	
	/**
	 * This method is used to load syncable data when a GUI is opened.
	 */
	@Override    
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readCommonNBT(pkt.getNbtCompound());
    	this.readSyncOnlyNBT(pkt.getNbtCompound());
    }
	
	/**
	 * This method will generate a message with partial updates for this TileEntity.
	 * 
	 * WARNING: Using this method resets the dirty flag and clears all pending updates up to this point.
	 * @return
	 */
	public PartialTileNBTUpdateMessage getPartialUpdateMessage() {
		
		PartialTileNBTUpdateMessage message = new PartialTileNBTUpdateMessage(getPos().getX(), getPos().getY(), getPos().getZ(), partialUpdateTag);
		dirty = false;
		partialUpdateTag = new NBTTagCompound();
		
		return message;
	}
	
	/**
	 * Utility method, so you don't have to remember to set dirty to true
	 */
	protected void addPartialUpdate(String fieldName, Integer value) {
		partialUpdateTag.setInteger(fieldName, value);
		dirty = true;
	}
	/**
	 * Utility method, so you don't have to remember to set dirty to true
	 */	
	protected void addPartialUpdate(String fieldName, String value) {
		partialUpdateTag.setString(fieldName, value);
		dirty = true;
	}
	/**
	 * Utility method, so you don't have to remember to set dirty to true
	 */	
	protected void addPartialUpdate(String fieldName, NBTBase value) {
		partialUpdateTag.setTag(fieldName, value);
		dirty = true;
	}
	/**
	 * Utility method, so you don't have to remember to set dirty to true
	 */	
	protected void addPartialUpdate(String fieldName, Boolean value) {
		partialUpdateTag.setBoolean(fieldName, value);
		dirty = true;
	}

	protected void addPartialUpdate(String fieldName, NBTTagCompound value) {
		partialUpdateTag.setTag(fieldName, value);
		dirty = true;
	}

	protected NBTTagCompound getCompoundTagFromPartialUpdate(String fieldName) {
		return partialUpdateTag.getCompoundTag(fieldName);
	}
	
	
	// Ignore these functions
	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		
	}

	@Override
	public int getFieldCount() {
		return 0;
	}
	
}