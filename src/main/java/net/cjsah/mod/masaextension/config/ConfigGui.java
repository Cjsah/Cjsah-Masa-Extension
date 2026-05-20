package net.cjsah.mod.masaextension.config;

import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import net.cjsah.mod.masaextension.ModInfo;

import java.util.List;

public class ConfigGui extends GuiConfigsBase {
    public static ConfigTab tab = ConfigTab.ALL;

    public ConfigGui() {
        super(10, 50, ModInfo.MOD_ID, null, ModInfo.MOD_NAME + " " + ModInfo.MOD_VERSION);
    }

    @Override
    public void initGui() {
        super.initGui();

        int x = 10;
        for (ConfigTab tab : ConfigTab.values()) {
            x += this.createButton(x, 26, tab);
        }
    }

    private int createButton(int x, int y, ConfigTab tab) {
        ButtonGeneric button = new ButtonGeneric(x, y, -1, 20, tab.getDisplayName());
        button.setEnabled(ConfigGui.tab != tab);
        this.addButton(button, new ButtonListener(tab, this));
        return button.getWidth() + 2;
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        return ConfigOptionWrapper.createFor(ConfigGui.tab.getConfigs());
    }

    private record ButtonListener(ConfigTab tab, ConfigGui parent) implements IButtonActionListener {
        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            ConfigGui.tab = this.tab;

            this.parent.reCreateListWidget();
            if (this.parent.getListWidget() != null) {
                this.parent.getListWidget().resetScrollbarPosition();
            }
            this.parent.initGui();
        }
    }

}
