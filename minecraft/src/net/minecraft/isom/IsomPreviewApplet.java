package net.minecraft.isom;

import java.applet.Applet;
import java.awt.BorderLayout;
import net.minecraft.src.CanvasIsomPreview;

public class IsomPreviewApplet extends Applet {
	private CanvasIsomPreview field_21003_a = new CanvasIsomPreview();

	public IsomPreviewApplet() {
		this.setLayout(new BorderLayout());
		this.add(this.field_21003_a, "Center");
	}

	public void start() {
		this.field_21003_a.func_1272_b();
	}

	public void stop() {
		this.field_21003_a.exit();
	}
}
