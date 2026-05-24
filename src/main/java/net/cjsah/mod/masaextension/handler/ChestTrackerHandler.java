package net.cjsah.mod.masaextension.handler;

import fi.dy.masa.litematica.materials.MaterialListUtils;
import fi.dy.masa.malilib.util.InventoryUtils;
import fi.dy.masa.malilib.util.ItemType;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.cjsah.mod.masaextension.util.InventoryUtil;
import net.cjsah.mod.masaextension.util.ModUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import red.jackf.chesttracker.api.ClientBlockSource;
import red.jackf.chesttracker.api.memory.Memory;
import red.jackf.chesttracker.api.memory.MemoryBank;
import red.jackf.chesttracker.api.memory.MemoryBankAccess;
import red.jackf.chesttracker.api.memory.MemoryKey;
import red.jackf.chesttracker.api.providers.MemoryBuilder;
import red.jackf.chesttracker.api.providers.defaults.DefaultProvider;
import red.jackf.chesttracker.impl.util.CachedClientBlockSource;
import red.jackf.whereisit.api.search.ConnectedBlocksGrabber;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ChestTrackerHandler {

    @SuppressWarnings("UnstableApiUsage")
    public static void saveItems(BlockPos pos, ListTag items) {
        if (!ModUtil.isChestTrackerLoaded()) return;

        MemoryBank bank = MemoryBankAccess.INSTANCE.getLoaded().orElse(null);
        if (bank == null) return;

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        RegistryAccess registryAccess = level.registryAccess();

        ClientBlockSource cbs = new CachedClientBlockSource(level, pos);
        var memoryLocation = DefaultProvider.INSTANCE.getMemoryLocation(cbs);
        if (memoryLocation.isEmpty() || memoryLocation.get().isOverride()) return;

        List<BlockPos> connectedBlocks = ConnectedBlocksGrabber.getConnected(cbs.level(), cbs.blockState(), cbs.pos());
        BlockPos rootPos = connectedBlocks.getFirst();

        List<ItemStack> stacks = InventoryUtil.serializeItems(items, registryAccess);

        Memory memory = MemoryBuilder.create(stacks)
            .inContainer(cbs.blockState().getBlock())
            .otherPositions(connectedBlocks.stream()
                .filter(it -> !it.equals(rootPos))
                .toList())
            .build();

        bank.addMemory(memoryLocation.get().memoryKey(), rootPos, memory);
    }

    public static void getSelectionContainerItems(List<BlockPos> positions, List<Object2IntOpenHashMap<ItemType>> containers) {
        if (!ModUtil.isChestTrackerLoaded()) return;

        MemoryBank bank = MemoryBankAccess.INSTANCE.getLoaded().orElse(null);
        if (bank == null) return;

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        Optional<MemoryKey> optional = bank.getKey(level.dimension().location());
        if (optional.isEmpty()) return;

        Map<BlockPos, Memory> memories = optional.get().getMemories();
        if (memories == null || memories.isEmpty()) return;

        for (BlockPos pos : positions) {
            Memory memory = memories.get(pos);
            if (memory == null) {
                continue;
            }

            containers.add(toItemCounter(memory));
        }
    }

    private static Object2IntOpenHashMap<ItemType> toItemCounter(Memory memory) {
        Object2IntOpenHashMap<ItemType> result = new Object2IntOpenHashMap<>();
        if (memory == null || memory.items() == null) {
            return result;
        }

        for (ItemStack stack : memory.items()) {
            if (stack == null || stack.isEmpty()) {
                continue;
            }

            // From `MaterialListUtils.getInventoryItemCounts#L174-L203`
            Item item = stack.getItem();
            if (item instanceof BlockItem blockItem &&
                blockItem.getBlock() instanceof ShulkerBoxBlock &&
                InventoryUtils.shulkerBoxHasItems(stack)
            ) {
                Object2IntOpenHashMap<ItemType> boxCounts = MaterialListUtils.getStoredItemCounts(stack);

                for (ItemType boxType : boxCounts.keySet()) {
                    result.addTo(boxType, boxCounts.getInt(boxType));
                }

                boxCounts.clear();
            } else if (item instanceof BundleItem && InventoryUtils.bundleHasItems(stack)) {
                Object2IntOpenHashMap<ItemType> bundleCounts = MaterialListUtils.getBundleItemCounts(stack);

                for (ItemType bundleType : bundleCounts.keySet()) {
                    result.addTo(bundleType, bundleCounts.getInt(bundleType));
                }

                bundleCounts.clear();
            } else {
                result.addTo(new ItemType(stack, true, false), stack.getCount());
            }
        }

        return result;
    }

}
