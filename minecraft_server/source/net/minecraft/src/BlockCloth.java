package net.minecraft.src;

public class BlockCloth extends Block {
	public BlockCloth() {
		super(35, 64, Material.cloth);
	}

	protected int damageDropped(int var1) {
		return var1;
	}

	public static int func_21033_c(int var0) {
		return ~var0 & 15;
	}

	public static int func_21034_d(int var0) {
		return ~var0 & 15;
	}
}
