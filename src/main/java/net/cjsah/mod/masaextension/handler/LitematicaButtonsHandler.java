package net.cjsah.mod.masaextension.handler;

import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import net.minecraft.Util;

import java.nio.file.Path;

public class LitematicaButtonsHandler {

    public static IButtonActionListener openFolderListener() {
        return (button, mouse) -> {
            Path path = DataManager.getSchematicsBaseDirectory().toPath();
            Util.getPlatform().openPath(path);
        };
    }
}
