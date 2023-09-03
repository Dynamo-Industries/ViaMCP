package vialoader;

import de.florianmichael.viamcp.ViaMCP;

public class ViaLoader {
    public static void load() {
        try {
            ViaMCP.create();
            ViaMCP.INSTANCE.initAsyncSlider();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
