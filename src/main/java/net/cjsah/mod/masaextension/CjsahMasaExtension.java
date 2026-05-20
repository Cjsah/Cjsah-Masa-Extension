package net.cjsah.mod.masaextension;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.registry.Registry;
import net.cjsah.mod.masaextension.config.ConfigGui;
import net.cjsah.mod.masaextension.config.Configs;
import net.cjsah.mod.masaextension.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//#if MC >= 12105
//$$ import java.util.Optional;
//#endif

// 同步容器, minihud/tweakroo预览同步chesttracker, 实体参与材料计算, 展示框, 容器内物品参与计数 投影添加打开文件夹

public class CjsahMasaExtension implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(ModInfo.MOD_NAME);

    private static String cachedServerName = "default";

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

    public static String getCachedServerName() {
        return cachedServerName;
    }

    public static void setCachedServerName(String name) {
        if (cachedServerName.equals(name)) return;
        cachedServerName = name;

        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().getChatListener().handleSystemMessage(
                Component.translatable("cjsah_masa_extension.server.changed", name),
                false
            );
        }

        LOGGER.info("Server is changed to: {}", name);
    }

    public static void reset() {
        cachedServerName = "default";
    }

    public static void applyExtension(CompoundTag metadata) {
        CompoundTag extension = new CompoundTag();
        extension.putString("server_name", ModConfig.getInstance().name());
        metadata.put("extra:" + ModInfo.MOD_ID, extension);
    }

    public static void parseMetadata(CompoundTag metadata) {
        String key = "extra:" + ModInfo.MOD_ID;
        //#if MC < 12105
        if (metadata.contains(key)) {
            CompoundTag compound = metadata.getCompound(key);
            CjsahMasaExtension.setCachedServerName(compound.getString("server_name"));
        }
        //#else
        //$$ Optional<CompoundTag> compound = metadata.getCompound(key);
        //$$ compound.ifPresent(tag ->
        //$$     CjsahMasaExtension.setCachedServerName(tag.getStringOr("server_name", "default"))
        //$$ );
        //#endif
    }
}
