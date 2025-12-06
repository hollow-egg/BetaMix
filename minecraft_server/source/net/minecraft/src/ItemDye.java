package net.minecraft.src;

public class ItemDye extends Item {
	public static final String[] dyeColors = new String[]{"black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "lightBlue", "magenta", "orange", "white"};

	public ItemDye(int var1) {
		super(var1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7) {
		if(var1.getItemDamage() == 15) {
			int var8 = var3.getBlockId(var4, var5, var6);
			if(var8 == Block.sapling.blockID) {
				((BlockSapling)Block.sapling).func_21027_b(var3, var4, var5, var6, var3.rand);
				--var1.stackSize;
				return true;
			}

			if(var8 == Block.crops.blockID) {
				((BlockCrops)Block.crops).func_21028_c(var3, var4, var5, var6);
				--var1.stackSize;
				return true;
			}
		}

		return false;
	}

	public void saddleEntity(ItemStack var1, EntityLiving var2) {
		if(var2 instanceof EntitySheep) {
			EntitySheep var3 = (EntitySheep)var2;
			int var4 = BlockCloth.func_21033_c(var1.getItemDamage());
			if(!var3.func_21069_f_() && var3.getFleeceColor() != var4) {
				var3.setFleeceColor(var4);
				--var1.stackSize;
			}
		}

	}
}
