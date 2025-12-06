package net.minecraft.src;

import java.util.List;

public class EntityFish extends Entity {
	private int tileX = -1;
	private int tileY = -1;
	private int tileZ = -1;
	private int field_4132_ai = 0;
	private boolean field_4129_aj = false;
	public int field_4134_a = 0;
	public EntityPlayer angler;
	private int field_6150_ak;
	private int field_4125_al = 0;
	private int field_4124_am = 0;
	public Entity field_20907_c = null;
	private int field_6149_an;
	private double field_6148_ao;
	private double field_6147_ap;
	private double field_6146_aq;
	private double field_6145_ar;
	private double field_6144_as;

	public EntityFish(World var1) {
		super(var1);
		this.setSize(0.25F, 0.25F);
	}

	protected void entityInit() {
	}

	public EntityFish(World var1, EntityPlayer var2) {
		super(var1);
		this.angler = var2;
		this.angler.fishEntity = this;
		this.setSize(0.25F, 0.25F);
		this.setLocationAndAngles(var2.posX, var2.posY + 1.62D - (double)var2.yOffset, var2.posZ, var2.rotationYaw, var2.rotationPitch);
		this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.posY -= (double)0.1F;
		this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.yOffset = 0.0F;
		float var3 = 0.4F;
		this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * var3);
		this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * var3);
		this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI) * var3);
		this.func_6142_a(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
	}

	public void func_6142_a(double var1, double var3, double var5, float var7, float var8) {
		float var9 = MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
		var1 /= (double)var9;
		var3 /= (double)var9;
		var5 /= (double)var9;
		var1 += this.rand.nextGaussian() * (double)0.0075F * (double)var8;
		var3 += this.rand.nextGaussian() * (double)0.0075F * (double)var8;
		var5 += this.rand.nextGaussian() * (double)0.0075F * (double)var8;
		var1 *= (double)var7;
		var3 *= (double)var7;
		var5 *= (double)var7;
		this.motionX = var1;
		this.motionY = var3;
		this.motionZ = var5;
		float var10 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
		this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(var1, var5) * 180.0D / (double)((float)Math.PI));
		this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(var3, (double)var10) * 180.0D / (double)((float)Math.PI));
		this.field_6150_ak = 0;
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.field_6149_an > 0) {
			double var21 = this.posX + (this.field_6148_ao - this.posX) / (double)this.field_6149_an;
			double var22 = this.posY + (this.field_6147_ap - this.posY) / (double)this.field_6149_an;
			double var23 = this.posZ + (this.field_6146_aq - this.posZ) / (double)this.field_6149_an;

			double var7;
			for(var7 = this.field_6145_ar - (double)this.rotationYaw; var7 < -180.0D; var7 += 360.0D) {
			}

			while(var7 >= 180.0D) {
				var7 -= 360.0D;
			}

			this.rotationYaw = (float)((double)this.rotationYaw + var7 / (double)this.field_6149_an);
			this.rotationPitch = (float)((double)this.rotationPitch + (this.field_6144_as - (double)this.rotationPitch) / (double)this.field_6149_an);
			--this.field_6149_an;
			this.setPosition(var21, var22, var23);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		} else {
			if(!this.worldObj.multiplayerWorld) {
				ItemStack var1 = this.angler.getCurrentEquippedItem();
				if(this.angler.isDead || !this.angler.isEntityAlive() || var1 == null || var1.getItem() != Item.fishingRod || this.getDistanceSqToEntity(this.angler) > 1024.0D) {
					this.setEntityDead();
					this.angler.fishEntity = null;
					return;
				}

				if(this.field_20907_c != null) {
					if(!this.field_20907_c.isDead) {
						this.posX = this.field_20907_c.posX;
						this.posY = this.field_20907_c.boundingBox.minY + (double)this.field_20907_c.height * 0.8D;
						this.posZ = this.field_20907_c.posZ;
						return;
					}

					this.field_20907_c = null;
				}
			}

			if(this.field_4134_a > 0) {
				--this.field_4134_a;
			}

			if(this.field_4129_aj) {
				int var19 = this.worldObj.getBlockId(this.tileX, this.tileY, this.tileZ);
				if(var19 == this.field_4132_ai) {
					++this.field_6150_ak;
					if(this.field_6150_ak == 1200) {
						this.setEntityDead();
					}

					return;
				}

				this.field_4129_aj = false;
				this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
				this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
				this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
				this.field_6150_ak = 0;
				this.field_4125_al = 0;
			} else {
				++this.field_4125_al;
			}

			Vec3D var20 = Vec3D.createVector(this.posX, this.posY, this.posZ);
			Vec3D var2 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var20, var2);
			var20 = Vec3D.createVector(this.posX, this.posY, this.posZ);
			var2 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			if(var3 != null) {
				var2 = Vec3D.createVector(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
			}

			Entity var4 = null;
			List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double var6 = 0.0D;

			double var13;
			for(int var8 = 0; var8 < var5.size(); ++var8) {
				Entity var9 = (Entity)var5.get(var8);
				if(var9.canBeCollidedWith() && (var9 != this.angler || this.field_4125_al >= 5)) {
					float var10 = 0.3F;
					AxisAlignedBB var11 = var9.boundingBox.expand((double)var10, (double)var10, (double)var10);
					MovingObjectPosition var12 = var11.func_706_a(var20, var2);
					if(var12 != null) {
						var13 = var20.distanceTo(var12.hitVec);
						if(var13 < var6 || var6 == 0.0D) {
							var4 = var9;
							var6 = var13;
						}
					}
				}
			}

			if(var4 != null) {
				var3 = new MovingObjectPosition(var4);
			}

			if(var3 != null) {
				if(var3.entityHit != null) {
					if(var3.entityHit.attackEntityFrom(this.angler, 0)) {
						this.field_20907_c = var3.entityHit;
					}
				} else {
					this.field_4129_aj = true;
				}
			}

			if(!this.field_4129_aj) {
				this.moveEntity(this.motionX, this.motionY, this.motionZ);
				float var24 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
				this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / (double)((float)Math.PI));

				for(this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var24) * 180.0D / (double)((float)Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
				}

				while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
					this.prevRotationPitch += 360.0F;
				}

				while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
					this.prevRotationYaw -= 360.0F;
				}

				while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
					this.prevRotationYaw += 360.0F;
				}

				this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
				this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
				float var25 = 0.92F;
				if(this.onGround || this.isCollidedHorizontally) {
					var25 = 0.5F;
				}

				byte var26 = 5;
				double var27 = 0.0D;

				for(int var28 = 0; var28 < var26; ++var28) {
					double var14 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(var28 + 0) / (double)var26 - 0.125D + 0.125D;
					double var16 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(var28 + 1) / (double)var26 - 0.125D + 0.125D;
					AxisAlignedBB var18 = AxisAlignedBB.getBoundingBoxFromPool(this.boundingBox.minX, var14, this.boundingBox.minZ, this.boundingBox.maxX, var16, this.boundingBox.maxZ);
					if(this.worldObj.isAABBInMaterial(var18, Material.water)) {
						var27 += 1.0D / (double)var26;
					}
				}

				if(var27 > 0.0D) {
					if(this.field_4124_am > 0) {
						--this.field_4124_am;
					} else if(this.rand.nextInt(500) == 0) {
						this.field_4124_am = this.rand.nextInt(30) + 10;
						this.motionY -= (double)0.2F;
						this.worldObj.playSoundAtEntity(this, "random.splash", 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
						float var29 = (float)MathHelper.floor_double(this.boundingBox.minY);

						float var15;
						int var30;
						float var31;
						for(var30 = 0; (float)var30 < 1.0F + this.width * 20.0F; ++var30) {
							var15 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
							var31 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
							this.worldObj.spawnParticle("bubble", this.posX + (double)var15, (double)(var29 + 1.0F), this.posZ + (double)var31, this.motionX, this.motionY - (double)(this.rand.nextFloat() * 0.2F), this.motionZ);
						}

						for(var30 = 0; (float)var30 < 1.0F + this.width * 20.0F; ++var30) {
							var15 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
							var31 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
							this.worldObj.spawnParticle("splash", this.posX + (double)var15, (double)(var29 + 1.0F), this.posZ + (double)var31, this.motionX, this.motionY, this.motionZ);
						}
					}
				}

				if(this.field_4124_am > 0) {
					this.motionY -= (double)(this.rand.nextFloat() * this.rand.nextFloat() * this.rand.nextFloat()) * 0.2D;
				}

				var13 = var27 * 2.0D - 1.0D;
				this.motionY += (double)0.04F * var13;
				if(var27 > 0.0D) {
					var25 = (float)((double)var25 * 0.9D);
					this.motionY *= 0.8D;
				}

				this.motionX *= (double)var25;
				this.motionY *= (double)var25;
				this.motionZ *= (double)var25;
				this.setPosition(this.posX, this.posY, this.posZ);
			}
		}
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		var1.setShort("xTile", (short)this.tileX);
		var1.setShort("yTile", (short)this.tileY);
		var1.setShort("zTile", (short)this.tileZ);
		var1.setByte("inTile", (byte)this.field_4132_ai);
		var1.setByte("shake", (byte)this.field_4134_a);
		var1.setByte("inGround", (byte)(this.field_4129_aj ? 1 : 0));
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		this.tileX = var1.getShort("xTile");
		this.tileY = var1.getShort("yTile");
		this.tileZ = var1.getShort("zTile");
		this.field_4132_ai = var1.getByte("inTile") & 255;
		this.field_4134_a = var1.getByte("shake") & 255;
		this.field_4129_aj = var1.getByte("inGround") == 1;
	}

	public int func_6143_c() {
		byte var1 = 0;
		if(this.field_20907_c != null) {
			double var2 = this.angler.posX - this.posX;
			double var4 = this.angler.posY - this.posY;
			double var6 = this.angler.posZ - this.posZ;
			double var8 = (double)MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
			double var10 = 0.1D;
			this.field_20907_c.motionX += var2 * var10;
			this.field_20907_c.motionY += var4 * var10 + (double)MathHelper.sqrt_double(var8) * 0.08D;
			this.field_20907_c.motionZ += var6 * var10;
			var1 = 3;
		} else if(this.field_4124_am > 0) {
			EntityItem var13 = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Item.fishRaw));
			double var3 = this.angler.posX - this.posX;
			double var5 = this.angler.posY - this.posY;
			double var7 = this.angler.posZ - this.posZ;
			double var9 = (double)MathHelper.sqrt_double(var3 * var3 + var5 * var5 + var7 * var7);
			double var11 = 0.1D;
			var13.motionX = var3 * var11;
			var13.motionY = var5 * var11 + (double)MathHelper.sqrt_double(var9) * 0.08D;
			var13.motionZ = var7 * var11;
			this.worldObj.entityJoinedWorld(var13);
			var1 = 1;
		}

		if(this.field_4129_aj) {
			var1 = 2;
		}

		this.setEntityDead();
		this.angler.fishEntity = null;
		return var1;
	}
}
