package net.cjsah.mod.masaextension.handler;

import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.selection.AreaSelection;
import fi.dy.masa.litematica.selection.Box;
import fi.dy.masa.litematica.selection.SelectionMode;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.ItemType;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.cjsah.mod.masaextension.config.Configs;
import net.cjsah.mod.masaextension.util.LitematicaUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

}
