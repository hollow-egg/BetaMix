package net.minecraft.src;

public class Slot {
	private final int slotIndex;
	private final IInventory inventory;
	public int field_20100_c;
	public int xDisplayPosition;
	public int yDisplayPosition;

	public Slot(IInventory var1, int var2, int var3, int var4) {
		this.inventory = var1;
		this.slotIndex = var2;
		this.xDisplayPosition = var3;
		this.yDisplayPosition = var4;
	}

	public void onPickupFromSlot() {
		this.onSlotChanged();
	}

	public boolean isItemValid(ItemStack var1) {
		return true;
	}

	public ItemStack getStack() {
		return this.inventory.getStackInSlot(this.slotIndex);
	}

	public void putStack(ItemStack var1) {
		this.inventory.setInventorySlotContents(this.slotIndex, var1);
		this.onSlotChanged();
	}

	public void onSlotChanged() {
		this.inventory.onInventoryChanged();
	}

	public int getSlotStackLimit() {
		return this.inventory.getInventoryStackLimit();
	}

	public ItemStack decrStackSize(int var1) {
		return this.inventory.decrStackSize(this.slotIndex, var1);
	}

	public boolean func_20090_a(IInventory var1, int var2) {
		return var1 == this.inventory && var2 == this.slotIndex;
	}
}
