package egg.betamix.screens;

import egg.betamix.StringTranslate;
import egg.betamix.config.Tweaks;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import org.lwjgl.input.Keyboard;

import static egg.betamix.config.Tweaks.PigsCook;
import static egg.betamix.config.Tweaks.PigsDropSaddles;

public class ConfigScreen extends GuiScreen {
    private GuiScreen parentScreen;

    public ConfigScreen(GuiScreen var1) {
        this.parentScreen = var1;
    }

    public void initGui() {
        StringTranslate var1 = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        this.controlList.clear();
        this.controlList.add(new GuiButton(0, 100, this.height - 30, 50, 20, var1.translateKey("config.back")));
        this.controlList.add(new GuiButton(1, this.width - 100, this.height - 30, 50, 20, var1.translateKey("config.save")));

        this.controlList.add(new GuiButton(2, 20, 40, 200, 20, var1.translateKey("config.PigsCook") + ":" + Boolean.toString(PigsCook)));
        this.controlList.add(new GuiButton(3, 20, 60, 200, 20, var1.translateKey("config.PigsDropSaddles") + ":" + Boolean.toString(PigsDropSaddles)));
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton var1) {
        switch (var1.id){
            case 0: {
                mc.displayGuiScreen(parentScreen);
                break;
            }
            case 1: {
                Tweaks.SaveTweakConfig();
                Tweaks.LoadTweakConfig();
                break;
            }
            case 2: {
                String[] split = var1.displayString.split(":");
                PigsCook = !PigsCook;
                split[1] = Boolean.toString(PigsCook);
                var1.displayString = String.format("%s:%s", split[0], split[1]);
                break;
            }
            case 3: {
                String[] split = var1.displayString.split(":");
                PigsDropSaddles = !PigsDropSaddles;
                split[1] = Boolean.toString(PigsDropSaddles);
                var1.displayString = String.format("%s:%s", split[0], split[1]);
                break;
            }
        }
    }

    private int func_4067_a(String var1, int var2) {
        try {
            return Integer.parseInt(var1.trim());
        } catch (Exception var4) {
            return var2;
        }
    }

    protected void keyTyped(char var1, int var2) {
        if(var1 == 27) {
            this.actionPerformed((GuiButton)this.controlList.get(0));
        }
    }

    public void drawScreen(int var1, int var2, float var3) {
        StringTranslate var4 = StringTranslate.getInstance();
        this.drawDefaultBackground();

        this.drawCenteredString(this.fontRenderer, var4.translateKey("config.title"), this.width / 2, 20, 16777215);

        super.drawScreen(var1, var2, var3);
    }
}