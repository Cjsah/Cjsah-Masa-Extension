package net.cjsah.mod.masaextension.handler;

import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.materials.MaterialCache;
import fi.dy.masa.litematica.materials.MaterialListEntry;
import fi.dy.masa.litematica.materials.MaterialListUtils;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.selection.AreaSelection;
import fi.dy.masa.litematica.selection.Box;
import fi.dy.masa.litematica.selection.SelectionMode;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.ItemType;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.cjsah.mod.masaextension.config.Configs;
import net.cjsah.mod.masaextension.mixin.compat.litematica.MaterialListUtilsInvoker;
import net.cjsah.mod.masaextension.util.EntityItemsUtil;
import net.cjsah.mod.masaextension.util.InventoryUtil;
import net.cjsah.mod.masaextension.util.LitematicaUtil;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class LitematicaHandler {

    public static IButtonActionListener openFolderListener() {
        return (button, mouse) -> {
            Path path = DataManager.getSchematicsBaseDirectory().toPath();
            Util.getPlatform().openPath(path);
        };
    }

    public static void applySelectionArea(Object2IntOpenHashMap<ItemType> items) {
        if (!Configs.SELECTION_MATERIALS.getBooleanValue()) {
            return;
        }

        List<BlockPos> positions = getSelectionPositions();
        if (positions.isEmpty()) {
            return;
        }

        List<Object2IntOpenHashMap<ItemType>> containers = new ObjectArrayList<>();
        ChestTrackerHandler.getSelectionContainerItems(positions, containers);

        for (Object2IntOpenHashMap<ItemType> itemMap : containers) {
            itemMap.forEach(items::addTo);
        }
    }

    public static List<BlockPos> getSelectionPositions() {
        AreaSelection selection = DataManager.getSelectionManager().getCurrentSelection();
        if (selection == null) return List.of();

        if (DataManager.getSelectionManager().getSelectionMode() == SelectionMode.NORMAL) {
            Set<BlockPos> positions = new ObjectOpenHashSet<>();

            for (Box box : selection.getAllSubRegionBoxes()) {
                positions.addAll(LitematicaUtil.boxToPosList(box));
            }

            return new ArrayList<>(positions);
        }

        Box box = selection.getSubRegionBox(DataManager.getSimpleArea().getName());
        return box != null ? LitematicaUtil.boxToPosList(box) : List.of();
    }

    public static List<MaterialListEntry> getMaterials(LitematicaSchematic schematic, Collection<String> subRegions, Object2IntOpenHashMap<BlockState> blocks, Player player) {
        List<MaterialListEntry> list = new ObjectArrayList<>();

        if (blocks.isEmpty()) return list;

        MaterialCache cache = MaterialCache.getInstance();
        Object2IntOpenHashMap<ItemType> items = new Object2IntOpenHashMap<>();
        MaterialListUtilsInvoker.invokeConvertStatesToStacks(blocks, items, cache);
        getExtraMaterials(schematic, subRegions, items);

        Object2IntOpenHashMap<ItemType> playerItems = MaterialListUtils.getInventoryItemCounts(player.getInventory());
        LitematicaHandler.applySelectionArea(playerItems);

        for (ItemType type : items.keySet()) {
            int count = items.getInt(type);
            list.add(new MaterialListEntry(type.getStack().copy(), count, count, 0, playerItems.getInt(type)));
        }

        return list;

    }

    private static void getExtraMaterials(LitematicaSchematic schematic, Collection<String> subRegions, Object2IntOpenHashMap<ItemType> items) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        for (String regionName : subRegions) {
            if (Configs.CONTAINER_MATERIALS.getBooleanValue()) {
                getBlockEntitiesMaterial(schematic.getBlockEntityMapForRegion(regionName), items, level);
            }
            if (Configs.ENTITY_MATERIALS.getBooleanValue()) {
                getEntitiesMaterial(schematic.getEntityListForRegion(regionName), items, level);
            }
        }
    }

    private static void getBlockEntitiesMaterial(Map<BlockPos, CompoundTag> blockEntities, Object2IntOpenHashMap<ItemType> items, Level level) {
        if (blockEntities == null || blockEntities.isEmpty()) return;

        for (CompoundTag nbt : blockEntities.values()) {
            List<ItemStack> stacks = InventoryUtil.serializeItems(nbt, level.registryAccess());
            if (stacks == null || stacks.isEmpty()) continue;

            for (ItemStack stack : stacks) {
                items.addTo(new ItemType(stack, true, false), stack.getCount());
            }
        }
    }

    private static void getEntitiesMaterial(List<LitematicaSchematic.EntityInfo> entities, Object2IntOpenHashMap<ItemType> items, Level level) {
        if (entities == null || entities.isEmpty()) return;

        Consumer<ItemStack> stackAppender = stack -> items.addTo(new ItemType(stack, true, false), stack.getCount());

        for (LitematicaSchematic.EntityInfo info : entities) {
            Optional<EntityType<?>> typeOptional = EntityType.by(info.nbt);
            if (typeOptional.isEmpty()) continue;
            EntityType<?> entityType = typeOptional.get();

            Optional<Entity> entityOptional = EntityType.create(info.nbt, level);
            if (entityOptional.isEmpty()) continue;
            Entity entity = entityOptional.get();

            EntityItemsUtil.countEntityItems(entityType, entity, stackAppender);
        }
    }

}
