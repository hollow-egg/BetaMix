package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;

public class NetServerHandler extends NetHandler implements ICommandListener {
	public static Logger logger = Logger.getLogger("Minecraft");
	public NetworkManager netManager;
	public boolean field_18_c = false;
	private MinecraftServer mcServer;
	private EntityPlayerMP playerEntity;
	private int field_15_f = 0;
	private double field_9009_g;
	private double field_9008_h;
	private double field_9007_i;
	private boolean field_9006_j = true;
	private Map field_10_k = new HashMap();

	public NetServerHandler(MinecraftServer var1, NetworkManager var2, EntityPlayerMP var3) {
		this.mcServer = var1;
		this.netManager = var2;
		var2.setNetHandler(this);
		this.playerEntity = var3;
		var3.field_20908_a = this;
	}

	public void func_42_a() {
		this.netManager.processReadPackets();
		if(this.field_15_f++ % 20 == 0) {
			this.netManager.addToSendQueue(new Packet0KeepAlive());
		}

	}

	public void func_43_c(String var1) {
		this.netManager.addToSendQueue(new Packet255KickDisconnect(var1));
		this.netManager.serverShutdown();
		this.mcServer.configManager.sendPacketToAllPlayers(new Packet3Chat("\u00a7e" + this.playerEntity.username + " left the game."));
		this.mcServer.configManager.playerLoggedOut(this.playerEntity);
		this.field_18_c = true;
	}

	public void handleFlying(Packet10Flying var1) {
		double var2;
		if(!this.field_9006_j) {
			var2 = var1.yPosition - this.field_9008_h;
			if(var1.xPosition == this.field_9009_g && var2 * var2 < 0.01D && var1.zPosition == this.field_9007_i) {
				this.field_9006_j = true;
			}
		}

		if(this.field_9006_j) {
			double var4;
			double var6;
			double var8;
			double var12;
			if(this.playerEntity.ridingEntity != null) {
				float var24 = this.playerEntity.rotationYaw;
				float var3 = this.playerEntity.rotationPitch;
				this.playerEntity.ridingEntity.updateRiderPosition();
				var4 = this.playerEntity.posX;
				var6 = this.playerEntity.posY;
				var8 = this.playerEntity.posZ;
				double var25 = 0.0D;
				var12 = 0.0D;
				if(var1.rotating) {
					var24 = var1.yaw;
					var3 = var1.pitch;
				}

				if(var1.moving && var1.yPosition == -999.0D && var1.stance == -999.0D) {
					var25 = var1.xPosition;
					var12 = var1.zPosition;
				}

				this.playerEntity.onGround = var1.onGround;
				this.playerEntity.func_175_i();
				this.playerEntity.moveEntity(var25, 0.0D, var12);
				this.playerEntity.setPositionAndRotation(var4, var6, var8, var24, var3);
				this.playerEntity.motionX = var25;
				this.playerEntity.motionZ = var12;
				if(this.playerEntity.ridingEntity != null) {
					this.mcServer.worldMngr.func_12017_b(this.playerEntity.ridingEntity, true);
				}

				if(this.playerEntity.ridingEntity != null) {
					this.playerEntity.ridingEntity.updateRiderPosition();
				}

				this.mcServer.configManager.func_613_b(this.playerEntity);
				this.field_9009_g = this.playerEntity.posX;
				this.field_9008_h = this.playerEntity.posY;
				this.field_9007_i = this.playerEntity.posZ;
				this.mcServer.worldMngr.updateEntity(this.playerEntity);
				return;
			}

			var2 = this.playerEntity.posY;
			this.field_9009_g = this.playerEntity.posX;
			this.field_9008_h = this.playerEntity.posY;
			this.field_9007_i = this.playerEntity.posZ;
			var4 = this.playerEntity.posX;
			var6 = this.playerEntity.posY;
			var8 = this.playerEntity.posZ;
			float var10 = this.playerEntity.rotationYaw;
			float var11 = this.playerEntity.rotationPitch;
			if(var1.moving && var1.yPosition == -999.0D && var1.stance == -999.0D) {
				var1.moving = false;
			}

			if(var1.moving) {
				var4 = var1.xPosition;
				var6 = var1.yPosition;
				var8 = var1.zPosition;
				var12 = var1.stance - var1.yPosition;
				if(var12 > 1.65D || var12 < 0.1D) {
					this.func_43_c("Illegal stance");
					logger.warning(this.playerEntity.username + " had an illegal stance: " + var12);
				}

				this.playerEntity.field_418_ai = var1.stance;
			}

			if(var1.rotating) {
				var10 = var1.yaw;
				var11 = var1.pitch;
			}

			this.playerEntity.func_175_i();
			this.playerEntity.ySize = 0.0F;
			this.playerEntity.setPositionAndRotation(this.field_9009_g, this.field_9008_h, this.field_9007_i, var10, var11);
			var12 = var4 - this.playerEntity.posX;
			double var14 = var6 - this.playerEntity.posY;
			double var16 = var8 - this.playerEntity.posZ;
			float var18 = 1.0F / 16.0F;
			boolean var19 = this.mcServer.worldMngr.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.boundingBox.copy().func_694_e((double)var18, (double)var18, (double)var18)).size() == 0;
			this.playerEntity.moveEntity(var12, var14, var16);
			var12 = var4 - this.playerEntity.posX;
			var14 = var6 - this.playerEntity.posY;
			if(var14 > -0.5D || var14 < 0.5D) {
				var14 = 0.0D;
			}

			var16 = var8 - this.playerEntity.posZ;
			double var20 = var12 * var12 + var14 * var14 + var16 * var16;
			boolean var22 = false;
			if(var20 > 1.0D / 16.0D) {
				var22 = true;
				logger.warning(this.playerEntity.username + " moved wrongly!");
				System.out.println("Got position " + var4 + ", " + var6 + ", " + var8);
				System.out.println("Expected " + this.playerEntity.posX + ", " + this.playerEntity.posY + ", " + this.playerEntity.posZ);
			}

			this.playerEntity.setPositionAndRotation(var4, var6, var8, var10, var11);
			boolean var23 = this.mcServer.worldMngr.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.boundingBox.copy().func_694_e((double)var18, (double)var18, (double)var18)).size() == 0;
			if(var19 && (var22 || !var23)) {
				this.func_41_a(this.field_9009_g, this.field_9008_h, this.field_9007_i, var10, var11);
				return;
			}

			this.playerEntity.onGround = var1.onGround;
			this.mcServer.configManager.func_613_b(this.playerEntity);
			this.playerEntity.func_9153_b(this.playerEntity.posY - var2, var1.onGround);
		}

	}

	public void func_41_a(double var1, double var3, double var5, float var7, float var8) {
		this.field_9006_j = false;
		this.field_9009_g = var1;
		this.field_9008_h = var3;
		this.field_9007_i = var5;
		this.playerEntity.setPositionAndRotation(var1, var3, var5, var7, var8);
		this.playerEntity.field_20908_a.sendPacket(new Packet13PlayerLookMove(var1, var3 + (double)1.62F, var3, var5, var7, var8, false));
	}

	public void handleBlockDig(Packet14BlockDig var1) {
		if(var1.status == 4) {
			this.playerEntity.func_161_a();
		} else {
			boolean var2 = this.mcServer.worldMngr.field_819_z = this.mcServer.configManager.isOp(this.playerEntity.username);
			boolean var3 = false;
			if(var1.status == 0) {
				var3 = true;
			}

			if(var1.status == 1) {
				var3 = true;
			}

			int var4 = var1.xPosition;
			int var5 = var1.yPosition;
			int var6 = var1.zPosition;
			if(var3) {
				double var7 = this.playerEntity.posX - ((double)var4 + 0.5D);
				double var9 = this.playerEntity.posY - ((double)var5 + 0.5D);
				double var11 = this.playerEntity.posZ - ((double)var6 + 0.5D);
				double var13 = var7 * var7 + var9 * var9 + var11 * var11;
				if(var13 > 36.0D) {
					return;
				}

				double var15 = this.playerEntity.posY;
				this.playerEntity.posY = this.playerEntity.field_418_ai;
				this.playerEntity.posY = var15;
			}

			int var18 = var1.face;
			int var8 = (int)MathHelper.abs((float)(var4 - this.mcServer.worldMngr.spawnX));
			int var19 = (int)MathHelper.abs((float)(var6 - this.mcServer.worldMngr.spawnZ));
			if(var8 > var19) {
				var19 = var8;
			}

			if(var1.status == 0) {
				if(var19 > 16 || var2) {
					this.playerEntity.field_425_ad.func_324_a(var4, var5, var6);
				}
			} else if(var1.status == 2) {
				this.playerEntity.field_425_ad.func_328_a();
			} else if(var1.status == 1) {
				if(var19 > 16 || var2) {
					this.playerEntity.field_425_ad.func_326_a(var4, var5, var6, var18);
				}
			} else if(var1.status == 3) {
				double var10 = this.playerEntity.posX - ((double)var4 + 0.5D);
				double var12 = this.playerEntity.posY - ((double)var5 + 0.5D);
				double var14 = this.playerEntity.posZ - ((double)var6 + 0.5D);
				double var16 = var10 * var10 + var12 * var12 + var14 * var14;
				if(var16 < 256.0D) {
					this.playerEntity.field_20908_a.sendPacket(new Packet53BlockChange(var4, var5, var6, this.mcServer.worldMngr));
				}
			}

			this.mcServer.worldMngr.field_819_z = false;
		}
	}

	public void handlePlace(Packet15Place var1) {
		ItemStack var2 = this.playerEntity.inventory.getCurrentItem();
		boolean var3 = this.mcServer.worldMngr.field_819_z = this.mcServer.configManager.isOp(this.playerEntity.username);
		if(var1.direction == 255) {
			if(var2 == null) {
				return;
			}

			this.playerEntity.field_425_ad.func_6154_a(this.playerEntity, this.mcServer.worldMngr, var2);
		} else {
			int var4 = var1.xPosition;
			int var5 = var1.yPosition;
			int var6 = var1.zPosition;
			int var7 = var1.direction;
			int var8 = (int)MathHelper.abs((float)(var4 - this.mcServer.worldMngr.spawnX));
			int var9 = (int)MathHelper.abs((float)(var6 - this.mcServer.worldMngr.spawnZ));
			if(var8 > var9) {
				var9 = var8;
			}

			if(var9 > 16 || var3) {
				this.playerEntity.field_425_ad.func_327_a(this.playerEntity, this.mcServer.worldMngr, var2, var4, var5, var6, var7);
			}

			this.playerEntity.field_20908_a.sendPacket(new Packet53BlockChange(var4, var5, var6, this.mcServer.worldMngr));
			if(var7 == 0) {
				--var5;
			}

			if(var7 == 1) {
				++var5;
			}

			if(var7 == 2) {
				--var6;
			}

			if(var7 == 3) {
				++var6;
			}

			if(var7 == 4) {
				--var4;
			}

			if(var7 == 5) {
				++var4;
			}

			this.playerEntity.field_20908_a.sendPacket(new Packet53BlockChange(var4, var5, var6, this.mcServer.worldMngr));
		}

		if(var2 != null && var2.stackSize == 0) {
			this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = null;
		}

		this.playerEntity.field_20064_am = true;
		this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = ItemStack.func_20117_a(this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem]);
		Slot var10 = this.playerEntity.craftingInventory.func_20127_a(this.playerEntity.inventory, this.playerEntity.inventory.currentItem);
		this.playerEntity.craftingInventory.func_20125_a();
		this.playerEntity.field_20064_am = false;
		if(!ItemStack.areItemStacksEqual(this.playerEntity.inventory.getCurrentItem(), var1.itemStack)) {
			this.sendPacket(new Packet103(this.playerEntity.craftingInventory.windowId, var10.field_20100_c, this.playerEntity.inventory.getCurrentItem()));
		}

		this.mcServer.worldMngr.field_819_z = false;
	}

	public void handleErrorMessage(String var1, Object[] var2) {
		logger.info(this.playerEntity.username + " lost connection: " + var1);
		this.mcServer.configManager.sendPacketToAllPlayers(new Packet3Chat("\u00a7e" + this.playerEntity.username + " left the game."));
		this.mcServer.configManager.playerLoggedOut(this.playerEntity);
		this.field_18_c = true;
	}

	public void registerPacket(Packet var1) {
		logger.warning(this.getClass() + " wasn\'t prepared to deal with a " + var1.getClass());
		this.func_43_c("Protocol error, unexpected packet");
	}

	public void sendPacket(Packet var1) {
		this.netManager.addToSendQueue(var1);
	}

	public void handleBlockItemSwitch(Packet16BlockItemSwitch var1) {
		this.playerEntity.inventory.currentItem = var1.id;
	}

	public void handleChat(Packet3Chat var1) {
		String var2 = var1.message;
		if(var2.length() > 100) {
			this.func_43_c("Chat message too long");
		} else {
			var2 = var2.trim();

			if(var2.startsWith("/")) {
				this.func_4010_d(var2);
			} else {
				var2 = "<" + this.playerEntity.username + "> " + var2;
				logger.info(var2);
				this.mcServer.configManager.sendPacketToAllPlayers(new Packet3Chat(var2));
			}

		}
	}

	private void func_4010_d(String var1) {
		if(var1.toLowerCase().startsWith("/me ")) {
			var1 = "* " + this.playerEntity.username + " " + var1.substring(var1.indexOf(" ")).trim();
			logger.info(var1);
			this.mcServer.configManager.sendPacketToAllPlayers(new Packet3Chat(var1));
		} else if(var1.toLowerCase().startsWith("/kill")) {
			this.playerEntity.attackEntityFrom((Entity)null, 1000);
		} else if(var1.toLowerCase().startsWith("/tell ")) {
			String[] var2 = var1.split(" ");
			if(var2.length >= 3) {
				var1 = var1.substring(var1.indexOf(" ")).trim();
				var1 = var1.substring(var1.indexOf(" ")).trim();
				var1 = "\u00a77" + this.playerEntity.username + " whispers " + var1;
				logger.info(var1 + " to " + var2[1]);
				if(!this.mcServer.configManager.sendPacketToPlayer(var2[1], new Packet3Chat(var1))) {
					this.sendPacket(new Packet3Chat("\u00a7cThere\'s no player by that name online."));
				}
			}
		} else {
			String var3;
			if(this.mcServer.configManager.isOp(this.playerEntity.username)) {
				var3 = var1.substring(1);
				logger.info(this.playerEntity.username + " issued server command: " + var3);
				this.mcServer.addCommand(var3, this);
			} else {
				var3 = var1.substring(1);
				logger.info(this.playerEntity.username + " tried command: " + var3);
			}
		}

	}

	public void handleArmAnimation(Packet18ArmAnimation var1) {
		if(var1.animate == 1) {
			this.playerEntity.swingItem();
		}

	}

	public void func_21001_a(Packet19 var1) {
		if(var1.state == 1) {
			this.playerEntity.func_21043_b(true);
		} else if(var1.state == 2) {
			this.playerEntity.func_21043_b(false);
		}

	}

	public void handleKickDisconnect(Packet255KickDisconnect var1) {
		this.netManager.networkShutdown("disconnect.quitting", new Object[0]);
	}

	public int func_38_b() {
		return this.netManager.getNumChunkDataPackets();
	}

	public void log(String var1) {
		this.sendPacket(new Packet3Chat("\u00a77" + var1));
	}

	public String getUsername() {
		return this.playerEntity.username;
	}

	public void func_6006_a(Packet7 var1) {
		Entity var2 = this.mcServer.worldMngr.func_6158_a(var1.targetEntity);
		if(var2 != null && this.playerEntity.canEntityBeSeen(var2)) {
			if(var1.isLeftClick == 0) {
				this.playerEntity.useCurrentItemOnEntity(var2);
			} else if(var1.isLeftClick == 1) {
				this.playerEntity.attackTargetEntityWithCurrentItem(var2);
			}
		}

	}

	public void func_9002_a(Packet9 var1) {
		if(this.playerEntity.health <= 0) {
			this.playerEntity = this.mcServer.configManager.func_9242_d(this.playerEntity);
		}
	}

	public void func_20006_a(Packet101 var1) {
		this.playerEntity.func_20059_K();
	}

	public void func_20007_a(Packet102 var1) {
		if(this.playerEntity.craftingInventory.windowId == var1.window_Id && this.playerEntity.craftingInventory.func_20124_c(this.playerEntity)) {
			ItemStack var2 = this.playerEntity.craftingInventory.func_20123_a(var1.inventorySlot, var1.mouseClick, this.playerEntity);
			if(ItemStack.areItemStacksEqual(var1.itemStack, var2)) {
				this.playerEntity.field_20908_a.sendPacket(new Packet106(var1.window_Id, var1.action, true));
				this.playerEntity.field_20064_am = true;
				this.playerEntity.craftingInventory.func_20125_a();
				this.playerEntity.func_20058_J();
				this.playerEntity.field_20064_am = false;
			} else {
				this.field_10_k.put(Integer.valueOf(this.playerEntity.craftingInventory.windowId), Short.valueOf(var1.action));
				this.playerEntity.field_20908_a.sendPacket(new Packet106(var1.window_Id, var1.action, false));
				this.playerEntity.craftingInventory.func_20129_a(this.playerEntity, false);
				ArrayList var3 = new ArrayList();

				for(int var4 = 0; var4 < this.playerEntity.craftingInventory.field_20135_e.size(); ++var4) {
					var3.add(((Slot)this.playerEntity.craftingInventory.field_20135_e.get(var4)).getStack());
				}

				this.playerEntity.func_20054_a(this.playerEntity.craftingInventory, var3);
			}
		}

	}

	public void func_20008_a(Packet106 var1) {
		Short var2 = (Short)this.field_10_k.get(Integer.valueOf(this.playerEntity.craftingInventory.windowId));
		if(var2 != null && var1.field_20033_b == var2.shortValue() && this.playerEntity.craftingInventory.windowId == var1.windowId && !this.playerEntity.craftingInventory.func_20124_c(this.playerEntity)) {
			this.playerEntity.craftingInventory.func_20129_a(this.playerEntity, true);
		}

	}

	public void func_20005_a(Packet130 var1) {
		if(this.mcServer.worldMngr.blockExists(var1.xPosition, var1.yPosition, var1.zPosition)) {
			TileEntity var2 = this.mcServer.worldMngr.getBlockTileEntity(var1.xPosition, var1.yPosition, var1.zPosition);

			int var3;
			int var5;
			for(var3 = 0; var3 < 4; ++var3) {
				boolean var4 = true;
				if(var1.signLines[var3].length() > 15) {
					var4 = false;
				} else {
					for(var5 = 0; var5 < var1.signLines[var3].length(); ++var5) {
						if(FontAllowedCharacters.field_20162_a.indexOf(var1.signLines[var3].charAt(var5)) < 0) {
							var4 = false;
						}
					}
				}

				if(!var4) {
					var1.signLines[var3] = "!?";
				}
			}

			if(var2 instanceof TileEntitySign) {
				var3 = var1.xPosition;
				int var8 = var1.yPosition;
				var5 = var1.zPosition;
				TileEntitySign var6 = (TileEntitySign)var2;

				for(int var7 = 0; var7 < 4; ++var7) {
					var6.signText[var7] = var1.signLines[var7];
				}

				var6.onInventoryChanged();
				this.mcServer.worldMngr.markBlockNeedsUpdate(var3, var8, var5);
			}
		}

	}
}
