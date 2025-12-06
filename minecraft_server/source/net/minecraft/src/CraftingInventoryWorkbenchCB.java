package net.minecraft.src;

public class CraftingInventoryWorkbenchCB extends CraftingInventoryCB {
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public IInventory craftResult = new InventoryCraftResult();
	private World field_20150_c;
	private int field_20149_h;
	private int field_20148_i;
	private int field_20147_j;

	public CraftingInventoryWorkbenchCB(InventoryPlayer var1, World var2, int var3, int var4, int var5) {
		this.field_20150_c = var2;
		this.field_20149_h = var3;
		this.field_20148_i = var4;
		this.field_20147_j = var5;
		this.func_20122_a(new SlotCrafting(this.craftMatrix, this.craftResult, 0, 124, 35));

		int var6;
		int var7;
		for(var6 = 0; var6 < 3; ++var6) {
			for(var7 = 0; var7 < 3; ++var7) {
				this.func_20122_a(new Slot(this.craftMatrix, var7 + var6 * 3, 30 + var7 * 18, 17 + var6 * 18));
			}
		}

		for(var6 = 0; var6 < 3; ++var6) {
			for(var7 = 0; var7 < 9; ++var7) {
				this.func_20122_a(new Slot(var1, var7 + var6 * 9 + 9, 8 + var7 * 18, 84 + var6 * 18));
			}
		}

		for(var6 = 0; var6 < 9; ++var6) {
			this.func_20122_a(new Slot(var1, var6, 8 + var6 * 18, 142));
		}

		this.onCraftMatrixChanged(this.craftMatrix);
	}

	public void onCraftMatrixChanged(IInventory var1) {
		this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix));
	}

	public void onCraftGuiClosed(EntityPlayer var1) {
		super.onCraftGuiClosed(var1);

		for(int var2 = 0; var2 < 9; ++var2) {
			ItemStack var3 = this.craftMatrix.getStackInSlot(var2);
			if(var3 != null) {
				var1.dropPlayerItem(var3);
			}
		}

	}

	public boolean func_20126_b(EntityPlayer var1) {
		return this.field_20150_c.getBlockId(this.field_20149_h, this.field_20148_i, this.field_20147_j) != Block.workbench.blockID ? false : var1.getDistanceSq((double)this.field_20149_h + 0.5D, (double)this.field_20148_i + 0.5D, (double)this.field_20147_j + 0.5D) <= 64.0D;
	}
}
