package net.minecraft.src;

public class CraftingInventoryFurnaceCB extends CraftingInventoryCB {
	private TileEntityFurnace field_20139_a;
	private int field_20138_b = 0;
	private int field_20141_c = 0;
	private int field_20140_h = 0;

	public CraftingInventoryFurnaceCB(IInventory var1, TileEntityFurnace var2) {
		this.field_20139_a = var2;
		this.func_20122_a(new Slot(var2, 0, 56, 17));
		this.func_20122_a(new Slot(var2, 1, 56, 53));
		this.func_20122_a(new Slot(var2, 2, 116, 35));

		int var3;
		for(var3 = 0; var3 < 3; ++var3) {
			for(int var4 = 0; var4 < 9; ++var4) {
				this.func_20122_a(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}

		for(var3 = 0; var3 < 9; ++var3) {
			this.func_20122_a(new Slot(var1, var3, 8 + var3 * 18, 142));
		}

	}

	public void func_20128_a(ICrafting var1) {
		super.func_20128_a(var1);
		var1.func_20056_a(this, 0, this.field_20139_a.furnaceCookTime);
		var1.func_20056_a(this, 1, this.field_20139_a.furnaceBurnTime);
		var1.func_20056_a(this, 2, this.field_20139_a.currentItemBurnTime);
	}

	public void func_20125_a() {
		super.func_20125_a();

		for(int var1 = 0; var1 < this.field_20133_g.size(); ++var1) {
			ICrafting var2 = (ICrafting)this.field_20133_g.get(var1);
			if(this.field_20138_b != this.field_20139_a.furnaceCookTime) {
				var2.func_20056_a(this, 0, this.field_20139_a.furnaceCookTime);
			}

			if(this.field_20141_c != this.field_20139_a.furnaceBurnTime) {
				var2.func_20056_a(this, 1, this.field_20139_a.furnaceBurnTime);
			}

			if(this.field_20140_h != this.field_20139_a.currentItemBurnTime) {
				var2.func_20056_a(this, 2, this.field_20139_a.currentItemBurnTime);
			}
		}

		this.field_20138_b = this.field_20139_a.furnaceCookTime;
		this.field_20141_c = this.field_20139_a.furnaceBurnTime;
		this.field_20140_h = this.field_20139_a.currentItemBurnTime;
	}

	public boolean func_20126_b(EntityPlayer var1) {
		return this.field_20139_a.canInteractWith(var1);
	}
}
