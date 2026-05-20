package net.cjsah.mod.masaextension;

import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import fi.dy.masa.malilib.hotkeys.IKeyboardInputHandler;
import fi.dy.masa.malilib.hotkeys.IMouseInputHandler;
import net.cjsah.mod.masaextension.config.Configs;

public class InputHandler implements IKeybindProvider, IKeyboardInputHandler, IMouseInputHandler {
    private static final InputHandler INSTANCE = new InputHandler();

    private InputHandler() {
    }

    public static InputHandler getInstance() {
        return INSTANCE;
    }


    @Override
    public void addKeysToMap(IKeybindManager manager) {
        for (IHotkey hotkey : Configs.HOTKEYS) {
            manager.addKeybindToMap(hotkey.getKeybind());
        }
    }

    @Override
    public void addHotkeys(IKeybindManager manager) {
    }
}
