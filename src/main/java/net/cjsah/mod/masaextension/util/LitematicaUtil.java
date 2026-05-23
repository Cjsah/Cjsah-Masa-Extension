package net.cjsah.mod.masaextension.util;

import fi.dy.masa.litematica.selection.Box;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class LitematicaUtil {
    public static List<BlockPos> boxToPosList(Box box) {
        Level level = Minecraft.getInstance().level;
        BlockPos pos1 = box.getPos1();
        BlockPos pos2 = box.getPos2();
        if (pos1 != null && pos2 != null) {
            int minX = Math.min(pos1.getX(), pos2.getX());
            int minY = Math.min(pos1.getY(), pos2.getY());
            int minZ = Math.min(pos1.getZ(), pos2.getZ());
            int maxX = Math.max(pos1.getX(), pos2.getX());
            int maxY = Math.max(pos1.getY(), pos2.getY());
            int maxZ = Math.max(pos1.getZ(), pos2.getZ());
            if (level != null) {
                minY = Math.max(level.getMinBuildHeight(), minY);
                maxY = Math.min(level.getMaxBuildHeight(), maxY);
            }

            BlockPos size = box.getSize();

            List<BlockPos> positions = new ArrayList<>(size.getX() * size.getY() * size.getZ());
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        positions.add(new BlockPos(x, y, z));
                    }
                }
            }

            return positions;
        } else if (pos1 == null && pos2 == null) {
            return List.of();
        } else {
            BlockPos pos = pos1 != null ? pos1 : pos2;
            if (level != null) {
                int y = pos.getY();
                if (y < level.getMinBuildHeight() || y > level.getMaxBuildHeight()) {
                    return List.of();
                }
            }
            return List.of(pos);
        }
    }

}
