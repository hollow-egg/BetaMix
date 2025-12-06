package net.minecraft.src;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.server.MinecraftServer;

public class WorldServer extends World {
	public ChunkProviderServer field_20911_y;
	public boolean field_819_z = false;
	public boolean field_816_A;
	private MinecraftServer field_6160_D;
	private MCHashTable field_20912_E = new MCHashTable();

	public WorldServer(MinecraftServer var1, File var2, String var3, int var4) {
		super(var2, var3, (new Random()).nextLong(), WorldProvider.func_4091_a(var4));
		this.field_6160_D = var1;
	}

	public void tick() {
		super.tick();
	}

	public void updateEntityWithOptionalForce(Entity var1, boolean var2) {
		if(!this.field_6160_D.noAnimals && (var1 instanceof EntityAnimals || var1 instanceof EntityWaterMob)) {
			var1.setEntityDead();
		}

		if(var1.riddenByEntity == null || !(var1.riddenByEntity instanceof EntityPlayer)) {
			super.updateEntityWithOptionalForce(var1, var2);
		}

	}

	public void func_12017_b(Entity var1, boolean var2) {
		super.updateEntityWithOptionalForce(var1, var2);
	}

	protected IChunkProvider getChunkProvider(File var1) {
		this.field_20911_y = new ChunkProviderServer(this, this.worldProvider.getChunkLoader(var1), this.worldProvider.getChunkProvider());
		return this.field_20911_y;
	}

	public List func_532_d(int var1, int var2, int var3, int var4, int var5, int var6) {
		ArrayList var7 = new ArrayList();

		for(int var8 = 0; var8 < this.loadedTileEntityList.size(); ++var8) {
			TileEntity var9 = (TileEntity)this.loadedTileEntityList.get(var8);
			if(var9.xCoord >= var1 && var9.yCoord >= var2 && var9.zCoord >= var3 && var9.xCoord < var4 && var9.yCoord < var5 && var9.zCoord < var6) {
				var7.add(var9);
			}
		}

		return var7;
	}

	public boolean func_6157_a(EntityPlayer var1, int var2, int var3, int var4) {
		int var5 = (int)MathHelper.abs((float)(var2 - this.spawnX));
		int var6 = (int)MathHelper.abs((float)(var4 - this.spawnZ));
		if(var5 > var6) {
			var6 = var5;
		}

		return var6 > 16 || this.field_6160_D.configManager.isOp(var1.username);
	}

	protected void obtainEntitySkin(Entity var1) {
		super.obtainEntitySkin(var1);
		this.field_20912_E.addKey(var1.entityId, var1);
	}

	protected void releaseEntitySkin(Entity var1) {
		super.releaseEntitySkin(var1);
		this.field_20912_E.removeObject(var1.entityId);
	}

	public Entity func_6158_a(int var1) {
		return (Entity)this.field_20912_E.lookup(var1);
	}

	public void func_9206_a(Entity var1, byte var2) {
		Packet38 var3 = new Packet38(var1.entityId, var2);
		this.field_6160_D.field_6028_k.func_609_a(var1, var3);
	}

	public Explosion newExplosion(Entity var1, double var2, double var4, double var6, float var8, boolean var9) {
		Explosion var10 = super.newExplosion(var1, var2, var4, var6, var8, var9);
		this.field_6160_D.configManager.func_12022_a(var2, var4, var6, 64.0D, new Packet60(var2, var4, var6, var8, var10.destroyedBlockPositions));
		return var10;
	}

	public void playNoteAt(int var1, int var2, int var3, int var4, int var5) {
		super.playNoteAt(var1, var2, var3, var4, var5);
		this.field_6160_D.configManager.func_12022_a((double)var1, (double)var2, (double)var3, 64.0D, new Packet54(var1, var2, var3, var4, var5));
	}
}
