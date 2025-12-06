package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class CraftingInventoryCB {
	public List field_20123_d = new ArrayList();
	public List field_20122_e = new ArrayList();
	public int windowId = 0;
	private short field_20917_a = 0;
	protected List field_20121_g = new ArrayList();
	private Set field_20918_b = new HashSet();

	protected void func_20117_a(Slot var1) {
		var1.field_20007_a = this.field_20122_e.size();
		this.field_20122_e.add(var1);
		this.field_20123_d.add((Object)null);
	}

	public void func_20114_a() {
		for(int var1 = 0; var1 < this.field_20122_e.size(); ++var1) {
			ItemStack var2 = ((Slot)this.field_20122_e.get(var1)).getStack();
			ItemStack var3 = (ItemStack)this.field_20123_d.get(var1);
			if(!ItemStack.areItemStacksEqual(var3, var2)) {
				var3 = var2 == null ? null : var2.copy();
				this.field_20123_d.set(var1, var3);

				for(int var4 = 0; var4 < this.field_20121_g.size(); ++var4) {
					((ICrafting)this.field_20121_g.get(var4)).func_20159_a(this, var1, var3);
				}
			}
		}

	}

	public Slot func_20118_a(int var1) {
		return (Slot)this.field_20122_e.get(var1);
	}

	public ItemStack func_20116_a(int var1, int var2, EntityPlayer var3) {
		ItemStack var4 = null;
		if(var2 == 0 || var2 == 1) {
			InventoryPlayer var5 = var3.inventory;
			if(var1 == -999) {
				if(var5.getItemStack() != null && var1 == -999) {
					if(var2 == 0) {
						var3.dropPlayerItem(var5.getItemStack());
						var5.setItemStack((ItemStack)null);
					}

					if(var2 == 1) {
						var3.dropPlayerItem(var5.getItemStack().splitStack(1));
						if(var5.getItemStack().stackSize == 0) {
							var5.setItemStack((ItemStack)null);
						}
					}
				}
			} else {
				Slot var6 = (Slot)this.field_20122_e.get(var1);
				if(var6 != null) {
					var6.onSlotChanged();
					ItemStack var7 = var6.getStack();
					if(var7 != null) {
						var4 = var7.copy();
					}

					if(var7 != null || var5.getItemStack() != null) {
						int var8;
						if(var7 != null && var5.getItemStack() == null) {
							var8 = var2 == 0 ? var7.stackSize : (var7.stackSize + 1) / 2;
							var5.setItemStack(var6.decrStackSize(var8));
							if(var7.stackSize == 0) {
								var6.putStack((ItemStack)null);
							}

							var6.onPickupFromSlot();
						} else if(var7 == null && var5.getItemStack() != null && var6.isItemValid(var5.getItemStack())) {
							var8 = var2 == 0 ? var5.getItemStack().stackSize : 1;
							if(var8 > var6.getSlotStackLimit()) {
								var8 = var6.getSlotStackLimit();
							}

							var6.putStack(var5.getItemStack().splitStack(var8));
							if(var5.getItemStack().stackSize == 0) {
								var5.setItemStack((ItemStack)null);
							}
						} else if(var7 != null && var5.getItemStack() != null) {
							if(var6.isItemValid(var5.getItemStack())) {
								if(var7.itemID != var5.getItemStack().itemID || var7.getHasSubtypes() && var7.getItemDamage() != var5.getItemStack().getItemDamage()) {
									if(var5.getItemStack().stackSize <= var6.getSlotStackLimit()) {
										var6.putStack(var5.getItemStack());
										var5.setItemStack(var7);
									}
								} else if(var7.itemID == var5.getItemStack().itemID) {
									if(var2 == 0) {
										var8 = var5.getItemStack().stackSize;
										if(var8 > var6.getSlotStackLimit() - var7.stackSize) {
											var8 = var6.getSlotStackLimit() - var7.stackSize;
										}

										if(var8 > var5.getItemStack().getMaxStackSize() - var7.stackSize) {
											var8 = var5.getItemStack().getMaxStackSize() - var7.stackSize;
										}

										var5.getItemStack().splitStack(var8);
										if(var5.getItemStack().stackSize == 0) {
											var5.setItemStack((ItemStack)null);
										}

										var7.stackSize += var8;
									} else if(var2 == 1) {
										var8 = 1;
										if(var8 > var6.getSlotStackLimit() - var7.stackSize) {
											var8 = var6.getSlotStackLimit() - var7.stackSize;
										}

										if(var8 > var5.getItemStack().getMaxStackSize() - var7.stackSize) {
											var8 = var5.getItemStack().getMaxStackSize() - var7.stackSize;
										}

										var5.getItemStack().splitStack(var8);
										if(var5.getItemStack().stackSize == 0) {
											var5.setItemStack((ItemStack)null);
										}

										var7.stackSize += var8;
									}
								}
							} else {
								ItemStack var10 = var5.getItemStack();
								if(var7.itemID == var10.itemID && var10.getMaxStackSize() > 1 && (!var7.getHasSubtypes() || var7.getItemDamage() == var10.getItemDamage())) {
									int var9 = var7.stackSize;
									if(var9 > 0 && var9 + var10.stackSize <= var10.getMaxStackSize()) {
										var10.stackSize += var9;
										var7.splitStack(var9);
										if(var7.stackSize == 0) {
											var6.putStack((ItemStack)null);
										}

										var6.onPickupFromSlot();
									}
								}
							}
						}
					}
				}
			}
		}

		return var4;
	}

	public void onCraftGuiClosed(EntityPlayer var1) {
		InventoryPlayer var2 = var1.inventory;
		if(var2.getItemStack() != null) {
			var1.dropPlayerItem(var2.getItemStack());
			var2.setItemStack((ItemStack)null);
		}

	}

	public void onCraftMatrixChanged(IInventory var1) {
		this.func_20114_a();
	}

	public void func_20119_a(int var1, ItemStack var2) {
		this.func_20118_a(var1).putStack(var2);
	}

	public void func_20115_a(ItemStack[] var1) {
		for(int var2 = 0; var2 < var1.length; ++var2) {
			this.func_20118_a(var2).putStack(var1[var2]);
		}

	}

	public void func_20112_a(int var1, int var2) {
	}

	public short func_20111_a(InventoryPlayer var1) {
		++this.field_20917_a;
		return this.field_20917_a;
	}

	public void func_20113_a(short var1) {
	}

	public void func_20110_b(short var1) {
	}

	public abstract boolean func_20120_b(EntityPlayer var1);
}
