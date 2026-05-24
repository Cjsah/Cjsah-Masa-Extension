package net.cjsah.mod.masaextension.handler;

import fi.dy.masa.litematica.materials.IMaterialList;
import fi.dy.masa.litematica.materials.MaterialListEntry;
import fi.dy.masa.litematica.materials.MaterialListUtils;
import fi.dy.masa.litematica.render.infohud.InfoHud;
import fi.dy.masa.litematica.scheduler.tasks.TaskCountBlocksPlacement;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TaskCountMoreBlocksPlacement extends TaskCountBlocksPlacement {
    public TaskCountMoreBlocksPlacement(SchematicPlacement schematicPlacement, IMaterialList materialList, boolean ignoreState) {
        super(schematicPlacement, materialList, ignoreState);
    }

    @Override
    protected void countAtPosition(BlockPos pos) {
        BlockState stateSchematic = this.schematicWorld.getBlockState(pos);
        BlockEntity blockEntitySchematic = this.schematicWorld.getBlockEntity(pos);

        if (!stateSchematic.isAir()) {
            BlockState stateClient = this.clientWorld.getBlockState(pos);
            BlockEntity blockEntityClient = this.clientWorld.getBlockEntity(pos);

            this.countsTotal.addTo(stateSchematic, 1);

            if (stateClient.isAir()) {
                this.countsMissing.addTo(stateSchematic, 1);
            } else if (stateClient != stateSchematic && (!this.ignoreState || stateClient.getBlock() != stateSchematic.getBlock())) {
                this.countsMissing.addTo(stateSchematic, 1);
                this.countsMismatch.addTo(stateSchematic, 1);
            } else if (blockEntitySchematic != null) {
                if (blockEntityClient == null || !blockEntityClient.equals(blockEntitySchematic)) {
                    this.countsMissing.addTo(stateSchematic, 1);
                    this.countsMismatch.addTo(stateSchematic, 1);
                }
            }
        }
    }

    @Override
    protected void onStop() {
        if (this.finished && this.isInWorld())
        {
            List<MaterialListEntry> list = MaterialListUtils.getMaterialList(
                this.countsTotal, this.countsMissing, this.countsMismatch, this.mc.player);
            this.materialList.setMaterialListEntries(list);
        }

        InfoHud.getInstance().removeInfoHudRenderer(this, false);

        this.notifyListener();
    }
}
