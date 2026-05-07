package net.cjsah.mod.masaextension.mixinplugin;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ConditionMixinPlugin implements IMixinConfigPlugin {
    private String prefix = "";

    @Override
    public void onLoad(String mixinPackage) {
        this.prefix = mixinPackage + ".";
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String trimmed = mixinClassName.substring(prefix.length());

        if (trimmed.startsWith("compat.")) {
            String modid = trimmed.split("\\.")[1];
            return FabricLoader.getInstance().isModLoaded(modid);
        }

        return true;
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
