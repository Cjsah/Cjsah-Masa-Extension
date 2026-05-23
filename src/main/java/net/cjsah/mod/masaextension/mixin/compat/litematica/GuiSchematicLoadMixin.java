package net.cjsah.mod.masaextension.mixin.compat.litematica;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import fi.dy.masa.litematica.gui.GuiSchematicBrowserBase;
import fi.dy.masa.litematica.gui.GuiSchematicLoad;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.util.StringUtils;
import net.cjsah.mod.masaextension.ModInfo;
import net.cjsah.mod.masaextension.handler.LitematicaHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiSchematicLoad.class, remap = false)
public abstract class GuiSchematicLoadMixin extends GuiSchematicBrowserBase {
    public GuiSchematicLoadMixin(int browserX, int browserY) {
        super(browserX, browserY);
    }

    @Inject(method = "initGui", at = @At("RETURN"))
    private void appendButton(CallbackInfo ci, @Local(ordinal = 0) LocalIntRef xRef, @Local(ordinal = 1) int y) {
        int x = xRef.get();

        String label = StringUtils.translate(ModInfo.MOD_ID + ".gui.button.open_folder");
        int width = this.getStringWidth(label) + 10;
        x = x - width - 4;
        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, label);

        this.addButton(button, LitematicaHandler.openFolderListener());
        xRef.set(x);
    }
}
