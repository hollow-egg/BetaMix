package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class ScreenShotHelper {
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
	private static ByteBuffer buffer;
	private static byte[] pixelData;
	private static int[] imageData;
	private int e;
	private DataOutputStream f;
	private byte[] g;
	private int h;
	private int i;
	private File j;

	public static String saveScreenshot(File var0, int var1, int var2) {
		try {
			File var3 = new File(var0, "screenshots");
			var3.mkdir();
			if(buffer == null || buffer.capacity() < var1 * var2) {
				buffer = BufferUtils.createByteBuffer(var1 * var2 * 3);
			}

			if(imageData == null || imageData.length < var1 * var2 * 3) {
				pixelData = new byte[var1 * var2 * 3];
				imageData = new int[var1 * var2];
			}

			GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			buffer.clear();
			GL11.glReadPixels(0, 0, var1, var2, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)buffer);
			buffer.clear();
			String var4 = "" + dateFormat.format(new Date());
			int var6 = 1;

			while(true) {
				File var5 = new File(var3, var4 + (var6 == 1 ? "" : "_" + var6) + ".png");
				if(!var5.exists()) {
					buffer.get(pixelData);

					for(int var7 = 0; var7 < var1; ++var7) {
						for(int var8 = 0; var8 < var2; ++var8) {
							int var9 = var7 + (var2 - var8 - 1) * var1;
							int var10 = pixelData[var9 * 3 + 0] & 255;
							int var11 = pixelData[var9 * 3 + 1] & 255;
							int var12 = pixelData[var9 * 3 + 2] & 255;
							int var13 = -16777216 | var10 << 16 | var11 << 8 | var12;
							imageData[var7 + var8 * var1] = var13;
						}
					}

					BufferedImage var15 = new BufferedImage(var1, var2, 1);
					var15.setRGB(0, 0, var1, var2, imageData, 0, var1);
					ImageIO.write(var15, "png", var5);
					return "Saved screenshot as " + var5.getName();
				}

				++var6;
			}
		} catch (Exception var14) {
			var14.printStackTrace();
			return "Failed to save: " + var14;
		}
	}

	public ScreenShotHelper(File var1, int var2, int var3, int var4) throws IOException {
		this.h = var2;
		this.i = var3;
		this.e = var4;
		File var5 = new File(var1, "screenshots");
		var5.mkdir();
		String var6 = "huge_" + dateFormat.format(new Date());

		for(int var7 = 1; (this.j = new File(var5, var6 + (var7 == 1 ? "" : "_" + var7) + ".tga")).exists(); ++var7) {
		}

		byte[] var8 = new byte[18];
		var8[2] = 2;
		var8[12] = (byte)(var2 % 256);
		var8[13] = (byte)(var2 / 256);
		var8[14] = (byte)(var3 % 256);
		var8[15] = (byte)(var3 / 256);
		var8[16] = 24;
		this.g = new byte[var2 * var4 * 3];
		this.f = new DataOutputStream(new FileOutputStream(this.j));
		this.f.write(var8);
	}

	public void func_21189_a(ByteBuffer var1, int var2, int var3, int var4, int var5) throws IOException {
		int var6 = var4;
		int var7 = var5;
		if(var4 > this.h - var2) {
			var6 = this.h - var2;
		}

		if(var5 > this.i - var3) {
			var7 = this.i - var3;
		}

		this.e = var7;

		for(int var8 = 0; var8 < var7; ++var8) {
			var1.position((var5 - var7) * var4 * 3 + var8 * var4 * 3);
			int var9 = (var2 + var8 * this.h) * 3;
			var1.get(this.g, var9, var6 * 3);
		}

	}

	public void func_21191_a() throws IOException {
		this.f.write(this.g, 0, this.h * 3 * this.e);
	}

	public String func_21190_b() throws IOException {
		this.f.close();
		return "Saved screenshot as " + this.j.getName();
	}
}
