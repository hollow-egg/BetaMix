package net.minecraft.src;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class SpawnerAnimals {
	private static Set eligibleChunksForSpawning = new HashSet();

	protected static ChunkPosition getRandomSpawningPointInChunk(World var0, int var1, int var2) {
		int var3 = var1 + var0.rand.nextInt(16);
		int var4 = var0.rand.nextInt(128);
		int var5 = var2 + var0.rand.nextInt(16);
		return new ChunkPosition(var3, var4, var5);
	}

	public static final int performSpawning(World var0, boolean var1, boolean var2) {
		if(!var1 && !var2) {
			return 0;
		} else {
			eligibleChunksForSpawning.clear();

			int var3;
			int var5;
			int var6;
			for(var3 = 0; var3 < var0.playerEntities.size(); ++var3) {
				EntityPlayer var4 = (EntityPlayer)var0.playerEntities.get(var3);
				var5 = MathHelper.floor_double(var4.posX / 16.0D);
				var6 = MathHelper.floor_double(var4.posZ / 16.0D);
				byte var7 = 8;

				for(int var8 = -var7; var8 <= var7; ++var8) {
					for(int var9 = -var7; var9 <= var7; ++var9) {
						eligibleChunksForSpawning.add(new ChunkCoordIntPair(var8 + var5, var9 + var6));
					}
				}
			}

			var3 = 0;
			EnumCreatureType[] var32 = EnumCreatureType.values();
			var5 = var32.length;

			label112:
			for(var6 = 0; var6 < var5; ++var6) {
				EnumCreatureType var33 = var32[var6];
				if((!var33.func_21103_d() || var2) && (var33.func_21103_d() || var1) && var0.countEntities(var33.getCreatureClass()) <= var33.getMaxNumberOfCreature() * eligibleChunksForSpawning.size() / 256) {
					Iterator var34 = eligibleChunksForSpawning.iterator();

					label109:
					while(true) {
						Class[] var11;
						int var12;
						int var14;
						int var15;
						int var16;
						do {
							do {
								ChunkCoordIntPair var35;
								do {
									do {
										if(!var34.hasNext()) {
											continue label112;
										}

										var35 = (ChunkCoordIntPair)var34.next();
										MobSpawnerBase var10 = var0.getWorldChunkManager().func_4066_a(var35);
										var11 = var10.getEntitiesForType(var33);
									} while(var11 == null);
								} while(var11.length == 0);

								var12 = var0.rand.nextInt(var11.length);
								ChunkPosition var13 = getRandomSpawningPointInChunk(var0, var35.chunkXPos * 16, var35.chunkZPos * 16);
								var14 = var13.x;
								var15 = var13.y;
								var16 = var13.z;
							} while(var0.isBlockOpaqueCube(var14, var15, var16));
						} while(var0.getBlockMaterial(var14, var15, var16) != var33.getCreatureMaterial());

						int var17 = 0;

						for(int var18 = 0; var18 < 3; ++var18) {
							int var19 = var14;
							int var20 = var15;
							int var21 = var16;
							byte var22 = 6;

							for(int var23 = 0; var23 < 4; ++var23) {
								var19 += var0.rand.nextInt(var22) - var0.rand.nextInt(var22);
								var20 += var0.rand.nextInt(1) - var0.rand.nextInt(1);
								var21 += var0.rand.nextInt(var22) - var0.rand.nextInt(var22);
								if(func_21167_a(var33, var0, var19, var20, var21)) {
									float var24 = (float)var19 + 0.5F;
									float var25 = (float)var20;
									float var26 = (float)var21 + 0.5F;
									if(var0.getClosestPlayer((double)var24, (double)var25, (double)var26, 24.0D) == null) {
										float var27 = var24 - (float)var0.spawnX;
										float var28 = var25 - (float)var0.spawnY;
										float var29 = var26 - (float)var0.spawnZ;
										float var30 = var27 * var27 + var28 * var28 + var29 * var29;
										if(var30 >= 576.0F) {
											EntityLiving var36;
											try {
												var36 = (EntityLiving)var11[var12].getConstructor(new Class[]{World.class}).newInstance(new Object[]{var0});
											} catch (Exception var31) {
												var31.printStackTrace();
												return var3;
											}

											var36.setLocationAndAngles((double)var24, (double)var25, (double)var26, var0.rand.nextFloat() * 360.0F, 0.0F);
											if(var36.getCanSpawnHere()) {
												++var17;
												var0.entityJoinedWorld(var36);
												func_21166_a(var36, var0, var24, var25, var26);
												if(var17 >= var36.getMaxSpawnedInChunk()) {
													continue label109;
												}
											}

											var3 += var17;
										}
									}
								}
							}
						}
					}
				}
			}

			return var3;
		}
	}

	private static boolean func_21167_a(EnumCreatureType var0, World var1, int var2, int var3, int var4) {
		return var0.getCreatureMaterial() == Material.water ? var1.getBlockMaterial(var2, var3, var4).getIsLiquid() && !var1.isBlockOpaqueCube(var2, var3 + 1, var4) : var1.isBlockOpaqueCube(var2, var3 - 1, var4) && !var1.isBlockOpaqueCube(var2, var3, var4) && !var1.getBlockMaterial(var2, var3, var4).getIsLiquid() && !var1.isBlockOpaqueCube(var2, var3 + 1, var4);
	}

	private static void func_21166_a(EntityLiving var0, World var1, float var2, float var3, float var4) {
		if(var0 instanceof EntitySpider && var1.rand.nextInt(100) == 0) {
			EntitySkeleton var5 = new EntitySkeleton(var1);
			var5.setLocationAndAngles((double)var2, (double)var3, (double)var4, var0.rotationYaw, 0.0F);
			var1.entityJoinedWorld(var5);
			var5.mountEntity(var0);
		} else if(var0 instanceof EntitySheep) {
			((EntitySheep)var0).setFleeceColor(EntitySheep.func_21066_a(var1.rand));
		}

	}
}
