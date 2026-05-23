package net.cjsah.mod.masaextension.handler;

import net.cjsah.mod.masaextension.util.ModUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import red.jackf.chesttracker.api.ClientBlockSource;
import red.jackf.chesttracker.api.memory.Memory;
import red.jackf.chesttracker.api.memory.MemoryBank;
import red.jackf.chesttracker.api.memory.MemoryBankAccess;
import red.jackf.chesttracker.api.providers.MemoryBuilder;
import red.jackf.chesttracker.api.providers.ProviderUtils;
import red.jackf.chesttracker.api.providers.defaults.DefaultProvider;
import red.jackf.chesttracker.impl.util.CachedClientBlockSource;
import red.jackf.whereisit.api.search.ConnectedBlocksGrabber;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ChestTrackerHandler {

    @SuppressWarnings("UnstableApiUsage")
    public static void saveItems(BlockPos pos, ListTag items) {
        System.out.println("save");
        if (!ModUtil.isChestTrackerLoaded() || items.isEmpty()) return;

        MemoryBank bank = MemoryBankAccess.INSTANCE.getLoaded().orElse(null);
        if (bank == null) return;

        Optional<ResourceLocation> key = ProviderUtils.getPlayersCurrentKey();
        if (key.isEmpty()) return;

        ClientLevel level = Minecraft.getInstance().level;
        RegistryAccess registryAccess = level.registryAccess();

        ClientBlockSource cbs = new CachedClientBlockSource(level, pos);
        var memoryLocation = DefaultProvider.INSTANCE.getMemoryLocation(cbs);
        if (memoryLocation.isEmpty() || memoryLocation.get().isOverride()) return;

        List<BlockPos> connectedBlocks = ConnectedBlocksGrabber.getConnected(cbs.level(), cbs.blockState(), cbs.pos());
        BlockPos rootPos = connectedBlocks.getFirst();

        List<ItemStack> stacks = items.stream()
            .map(tag -> ItemStack.parse(registryAccess, tag).orElse(ItemStack.EMPTY))
            .filter(stack -> !stack.isEmpty())
            .toList();

        Memory memory = MemoryBuilder.create(stacks)
            .inContainer(cbs.blockState().getBlock())
            .otherPositions(connectedBlocks.stream()
                .filter(it -> !it.equals(rootPos))
                .toList())
            .build();

        bank.addMemory(memoryLocation.get().memoryKey(), rootPos, memory);
    }
}
