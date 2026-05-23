package net.cjsah.mod.masaextension.config.io;

import com.google.gson.JsonObject;
import net.cjsah.mod.masaextension.handler.CrossServerHandler;

public class ConfigServerIO extends ConfigIO {

    protected ConfigServerIO() {
        super("server");
    }

    @Override
    protected void loadJson(JsonObject root) {
        CrossServerHandler.load(root);
    }

    @Override
    protected void saveJson(JsonObject root) {
        CrossServerHandler.save(root);
    }
}
