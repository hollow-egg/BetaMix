package egg.betamix.config;

import java.io.*;

public class Tweaks {
    private static String configFileName = "BetaMixTweaks.txt";
    public static boolean PigsCook = false;
    public static boolean PigsDropSaddles = false;

    public static void SaveTweakConfig(){
        File file = new File(configFileName);
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(file, false));

            writer.println("PigsCook=" + Boolean.toString(PigsCook));
            writer.println("PigsDropSaddles=" + Boolean.toString(PigsDropSaddles));

            writer.close();
        } catch (Exception var5) {
            System.out.println("Failed to save betamix tweak config");
            var5.printStackTrace();
        }
    }

    public static void LoadTweakConfig(){
        File file = new File(configFileName);

        try {
            if(!file.exists()) {
                SaveTweakConfig();
                return;
            }

            BufferedReader var1 = new BufferedReader(new FileReader(file));
            String var2 = "";

            while(true) {
                var2 = var1.readLine();
                if(var2 == null) {
                    var1.close();
                    break;
                }
                String[] var3 = var2.split("=");
                if(var3[0].equals("PigsCook")) {
                    PigsCook = Boolean.parseBoolean(var3[1]);
                    System.out.println("PigsCook="+var3[1]);
                }
                if(var3[0].equals("PigsDropSaddles")) {
                    PigsDropSaddles = Boolean.parseBoolean(var3[1]);
                    System.out.println("PigsDropSaddles="+var3[1]);
                }
            }
            var1.close();
        } catch (Exception var5) {
            System.out.println("Failed to load betamix tweak config");
            var5.printStackTrace();
        }
    }
}
