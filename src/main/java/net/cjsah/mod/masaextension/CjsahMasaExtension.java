package net.cjsah.mod.masaextension;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.registry.Registry;
import net.cjsah.mod.masaextension.config.ConfigGui;
import net.cjsah.mod.masaextension.config.Configs;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CjsahMasaExtension implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(ModInfo.MOD_NAME);

    @Override
    public void onInitialize() {
        InitializationHandler.getInstance().registerInitializationHandler(() -> {
            ConfigManager.getInstance().registerConfigHandler(ModInfo.MOD_ID, new Configs());
            Registry.CONFIG_SCREEN.registerConfigScreenFactory(
                new fi.dy.masa.malilib.util.data.ModInfo(ModInfo.MOD_ID, ModInfo.MOD_NAME, ConfigGui::new)
            );

            InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
            InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());
            InputEventHandler.getInputManager().registerMouseInputHandler(InputHandler.getInstance());

            Configs.OPEN_CONFIG_SCREEN.getKeybind().setCallback(((action, key) -> {
                GuiBase.openGui(new ConfigGui());
                return true;
            }));
            Configs.SYNC_AREA_CONTAINER.getKeybind().setCallback(((action, key) -> {
                //TODO: 发送同步容器请求
                return true;
            }));

        });
    }
}
