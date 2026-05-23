package net.cjsah.mod.masaextension;

import net.cjsah.mod.masaextension.config.io.ConfigIO;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CjsahMasaExtension implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(ModInfo.MOD_NAME);
    public static final ConfigIO ServerConfig = ConfigIO.server();

    @Override
    public void onInitialize() {
        ServerConfig.load();
    }
}
