package net.minecraft.src;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.minecraft.server.MinecraftServer;

public class EntityPlayerMP extends EntityPlayer implements ICrafting {
	public NetServerHandler field_20908_a;
	public MinecraftServer mcServer;
	public ItemInWorldManager field_425_ad;
	public double field_9155_d;
	public double field_9154_e;
	public List loadedChunks = new LinkedList();
	public Set field_420_ah = new HashSet();
	public double field_418_ai;
	private int field_9156_bu = -99999999;
	private int field_15004_bw = 60;
	private ItemStack[] playerInventory = new ItemStack[]{null, null, null, null, null};
	private int field_20065_bH = 0;
	public boolean field_20064_am;

	public EntityPlayerMP(MinecraftServer var1, World var2, String var3, ItemInWorldManager var4) {
		super(var2);
		int var5 = var2.spawnX;
		int var6 = var2.spawnZ;
		int var7 = var2.spawnY;
		if(!var2.worldProvider.field_4306_c) {
			var5 += this.rand.nextInt(20) - 10;
			var7 = var2.findTopSolidBlock(var5, var6);
			var6 += this.rand.nextInt(20) - 10;
		}

		this.setLocationAndAngles((double)var5 + 0.5D, (double)var7, (double)var6 + 0.5D, 0.0F, 0.0F);
		this.mcServer = var1;
		this.stepHeight = 0.0F;
		var4.field_675_a = this;
		this.username = var3;
		this.field_425_ad = var4;
		this.yOffset = 0.0F;
	}

	public void func_20057_k() {
		this.craftingInventory.func_20128_a(this);
	}

	public ItemStack[] getInventory() {
		return this.playerInventory;
	}

	public void onUpdate() {
		--this.field_15004_bw;
		this.craftingInventory.func_20125_a();

		for(int var1 = 0; var1 < 5; ++var1) {
			ItemStack var2 = this.func_21073_a(var1);
			if(var2 != this.playerInventory[var1]) {
				this.mcServer.field_6028_k.func_12021_a(this, new Packet5PlayerInventory(this.entityId, var1, var2));
				this.playerInventory[var1] = var2;
			}
		}

	}

	public ItemStack func_21073_a(int var1) {
		return var1 == 0 ? this.inventory.getCurrentItem() : this.inventory.armorInventory[var1 - 1];
	}

	public void onDeath(Entity var1) {
		this.inventory.dropAllItems();
	}

	public boolean attackEntityFrom(Entity var1, int var2) {
		if(this.field_15004_bw > 0) {
			return false;
		} else {
			if(!this.mcServer.field_9011_n) {
				if(var1 instanceof EntityPlayer) {
					return false;
				}

				if(var1 instanceof EntityArrow) {
					EntityArrow var3 = (EntityArrow)var1;
					if(var3.field_439_ah instanceof EntityPlayer) {
						return false;
					}
				}
			}

			return super.attackEntityFrom(var1, var2);
		}
	}

	public void heal(int var1) {
		super.heal(var1);
	}

	public void func_175_i() {
		super.onUpdate();
		ChunkCoordIntPair var1 = null;
		double var2 = 0.0D;

		for(int var4 = 0; var4 < this.loadedChunks.size(); ++var4) {
			ChunkCoordIntPair var5 = (ChunkCoordIntPair)this.loadedChunks.get(var4);
			double var6 = var5.func_73_a(this);
			if(var4 == 0 || var6 < var2) {
				var1 = var5;
				var2 = var5.func_73_a(this);
			}
		}

		if(var1 != null) {
			boolean var8 = false;
			if(var2 < 1024.0D) {
				var8 = true;
			}

			if(this.field_20908_a.func_38_b() < 2) {
				var8 = true;
			}

			if(var8) {
				this.loadedChunks.remove(var1);
				this.field_20908_a.sendPacket(new Packet51MapChunk(var1.chunkXPos * 16, 0, var1.chunkZPos * 16, 16, 128, 16, this.mcServer.worldMngr));
				List var9 = this.mcServer.worldMngr.func_532_d(var1.chunkXPos * 16, 0, var1.chunkZPos * 16, var1.chunkXPos * 16 + 16, 128, var1.chunkZPos * 16 + 16);

				for(int var10 = 0; var10 < var9.size(); ++var10) {
					this.func_20063_a((TileEntity)var9.get(var10));
				}
			}
		}

		if(this.health != this.field_9156_bu) {
			this.field_20908_a.sendPacket(new Packet8(this.health));
			this.field_9156_bu = this.health;
		}

	}

	private void func_20063_a(TileEntity var1) {
		if(var1 != null) {
			Packet var2 = var1.func_20070_f();
			if(var2 != null) {
				this.field_20908_a.sendPacket(var2);
			}
		}

	}

	public void onLivingUpdate() {
		this.motionX = this.motionY = this.motionZ = 0.0D;
		this.isJumping = false;
		super.onLivingUpdate();
	}

	public void onItemPickup(Entity var1, int var2) {
		if(!var1.isDead) {
			if(var1 instanceof EntityItem) {
				this.mcServer.field_6028_k.func_12021_a(var1, new Packet22Collect(var1.entityId, this.entityId));
			}

			if(var1 instanceof EntityArrow) {
				this.mcServer.field_6028_k.func_12021_a(var1, new Packet22Collect(var1.entityId, this.entityId));
			}
		}

		super.onItemPickup(var1, var2);
		this.craftingInventory.func_20125_a();
	}

	public void swingItem() {
		if(!this.isSwinging) {
			this.swingProgressInt = -1;
			this.isSwinging = true;
			this.mcServer.field_6028_k.func_12021_a(this, new Packet18ArmAnimation(this, 1));
		}

	}

	public float getEyeHeight() {
		return 1.62F;
	}

	public void mountEntity(Entity var1) {
		super.mountEntity(var1);
		this.field_20908_a.sendPacket(new Packet39(this, this.ridingEntity));
		this.field_20908_a.func_41_a(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
	}

	protected void updateFallState(double var1, boolean var3) {
	}

	public void func_9153_b(double var1, boolean var3) {
		super.updateFallState(var1, var3);
	}

	private void func_20060_R() {
		this.field_20065_bH = this.field_20065_bH % 100 + 1;
	}

	public void displayWorkbenchGUI(int var1, int var2, int var3) {
		this.func_20060_R();
		this.field_20908_a.sendPacket(new Packet100(this.field_20065_bH, 1, "Crafting", 9));
		this.craftingInventory = new CraftingInventoryWorkbenchCB(this.inventory, this.worldObj, var1, var2, var3);
		this.craftingInventory.windowId = this.field_20065_bH;
		this.craftingInventory.func_20128_a(this);
	}

	public void displayGUIChest(IInventory var1) {
		this.func_20060_R();
		this.field_20908_a.sendPacket(new Packet100(this.field_20065_bH, 0, var1.getInvName(), var1.getSizeInventory()));
		this.craftingInventory = new CraftingInventoryChestCB(this.inventory, var1);
		this.craftingInventory.windowId = this.field_20065_bH;
		this.craftingInventory.func_20128_a(this);
	}

	public void displayGUIFurnace(TileEntityFurnace var1) {
		this.func_20060_R();
		this.field_20908_a.sendPacket(new Packet100(this.field_20065_bH, 2, var1.getInvName(), var1.getSizeInventory()));
		this.craftingInventory = new CraftingInventoryFurnaceCB(this.inventory, var1);
		this.craftingInventory.windowId = this.field_20065_bH;
		this.craftingInventory.func_20128_a(this);
	}

	public void displayGUIDispenser(TileEntityDispenser var1) {
		this.func_20060_R();
		this.field_20908_a.sendPacket(new Packet100(this.field_20065_bH, 3, var1.getInvName(), var1.getSizeInventory()));
		this.craftingInventory = new CraftingInventoryDispenserCB(this.inventory, var1);
		this.craftingInventory.windowId = this.field_20065_bH;
		this.craftingInventory.func_20128_a(this);
	}

	public void func_20055_a(CraftingInventoryCB var1, int var2, ItemStack var3) {
		if(!(var1.func_20120_a(var2) instanceof SlotCrafting)) {
			if(!this.field_20064_am) {
				this.field_20908_a.sendPacket(new Packet103(var1.windowId, var2, var3));
			}
		}
	}

	public void func_20054_a(CraftingInventoryCB var1, List var2) {
		this.field_20908_a.sendPacket(new Packet104(var1.windowId, var2));
		this.field_20908_a.sendPacket(new Packet103(-1, -1, this.inventory.getItemStack()));
	}

	public void func_20056_a(CraftingInventoryCB var1, int var2, int var3) {
		this.field_20908_a.sendPacket(new Packet105(var1.windowId, var2, var3));
	}

	public void onItemStackChanged(ItemStack var1) {
	}

	public void func_20043_I() {
		this.field_20908_a.sendPacket(new Packet101(this.craftingInventory.windowId));
		this.func_20059_K();
	}

	public void func_20058_J() {
		if(!this.field_20064_am) {
			this.field_20908_a.sendPacket(new Packet103(-1, -1, this.inventory.getItemStack()));
		}
	}

	public void func_20059_K() {
		this.craftingInventory.onCraftGuiClosed(this);
		this.craftingInventory = this.field_20053_ao;
	}
}
