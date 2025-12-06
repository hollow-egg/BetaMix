package net.minecraft.src;

public class GuiOptions extends GuiScreen {
	private GuiScreen parentScreen;
	protected String screenTitle = "Options";
	private GameSettings options;

	public GuiOptions(GuiScreen var1, GameSettings var2) {
		this.parentScreen = var1;
		this.options = var2;
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.screenTitle = var1.translateKey("options.title");
		EnumOptions[] var2 = EnumOptions.values();
		int var3 = var2.length;

		for(int var4 = 0; var4 < var3; ++var4) {
			EnumOptions var5 = var2[var4];
			int var6 = var5.returnEnumOrdinal();
			if(!var5.getEnumFloat()) {
				this.controlList.add(new GuiSmallButton(var5.returnEnumOrdinal(), this.width / 2 - 155 + var6 % 2 * 160, this.height / 6 + 24 * (var6 >> 1), var5, this.options.getKeyBinding(var5)));
			} else {
				this.controlList.add(new GuiSlider(var5.returnEnumOrdinal(), this.width / 2 - 155 + var6 % 2 * 160, this.height / 6 + 24 * (var6 >> 1), var5, this.options.getKeyBinding(var5), this.options.getOptionFloatValue(var5)));
			}
		}

		this.controlList.add(new GuiButton(100, this.width / 2 - 100, this.height / 6 + 120 + 12, var1.translateKey("options.controls")));
		this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, var1.translateKey("gui.done")));
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id < 100 && var1 instanceof GuiSmallButton) {
				this.options.setOptionValue(((GuiSmallButton)var1).returnEnumOptions(), 1);
				var1.displayString = this.options.getKeyBinding(EnumOptions.func_20137_a(var1.id));
			}

			if(var1.id == 100) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(new GuiControls(this, this.options));
			}

			if(var1.id == 200) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(this.parentScreen);
			}

		}
	}

	public void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
		super.drawScreen(var1, var2, var3);
	}
}
