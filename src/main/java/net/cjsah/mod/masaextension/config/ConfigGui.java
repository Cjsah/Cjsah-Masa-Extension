package net.cjsah.mod.masaextension.config;

import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.StringUtils;
import net.cjsah.mod.masaextension.ModInfo;

import java.util.List;

public class ConfigGui extends GuiConfigsBase {
    public static Tab tab = Tab.GENERIC;

    public ConfigGui() {
        super(10, 50, ModInfo.MOD_ID, null, ModInfo.MOD_NAME + " " + ModInfo.MOD_VERSION);
    }

    @Override
    public void initGui() {
        super.initGui();

        int x = 10;
        int y = 26;

        for (Tab tab : Tab.values()) {
            if (tab == Tab.ALL) continue;

            int width = this.getStringWidth(tab.getDisplayName()) + 10;
            if (x >= this.getScreenWidth() - width - 10) {
                x = 10;
                y += 22;
            }

            x += this.createButton(x, y, width, tab);

        }

    }

    private int createButton(int x, int y, int width, Tab tab) {
        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, tab.getDisplayName());
        button.setEnabled(ConfigGui.tab != tab);
        this.addButton(button, new ConfigGui.ButtonListenerConfigTabs(tab, this));

        return button.getWidth() + 2;
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        return ConfigOptionWrapper.createFor(Configs.OPTIONS);
    }

    public enum Tab {
        ALL,
        GENERIC;

        public final String key = ModInfo.MOD_ID + ".config." + this.name().toLowerCase();

        public String getDisplayName() {
            return StringUtils.translate(this.key);
        }

    }

    private record ButtonListenerConfigTabs(Tab tab, ConfigGui parent) implements IButtonActionListener {
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
