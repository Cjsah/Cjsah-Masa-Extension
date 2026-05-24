package net.cjsah.mod.masaextension.util;

import net.cjsah.mod.masaextension.mixin.ItemFrameAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EntityItemsUtil {
    private static final Map<EntityType<?>, BiConsumer<Entity, Consumer<ItemStack>>> ENTITY_ITEM_COUNTER = Map.of(
        EntityType.ITEM_FRAME, EntityItemsUtil::itemFrameItems,
        EntityType.GLOW_ITEM_FRAME, EntityItemsUtil::itemFrameItems,
        EntityType.MINECART, EntityItemsUtil::minecartItems,
        EntityType.HOPPER_MINECART, EntityItemsUtil::minecartItems,
        EntityType.CHEST_MINECART, EntityItemsUtil::minecartItems,
        EntityType.FURNACE_MINECART, EntityItemsUtil::minecartItems,
        EntityType.TNT_MINECART, EntityItemsUtil::minecartItems,
        EntityType.COMMAND_BLOCK_MINECART, EntityItemsUtil::minecartItems
    );

    public static void countEntityItems(EntityType<?> entityType, Entity entity, Consumer<ItemStack> stackAppender) {
        BiConsumer<Entity, Consumer<ItemStack>> resolver = ENTITY_ITEM_COUNTER.get(entityType);
        if (resolver == null) return;
        resolver.accept(entity, stackAppender);
    }

    private static void itemFrameItems(Entity entity, Consumer<ItemStack> appender) {
        if (!(entity instanceof ItemFrame frame)) return;
        appender.accept(((ItemFrameAccessor) frame).invokeGetFrameItemStack());
        appender.accept(frame.getItem());
    }

    public static void minecartItems(Entity entity, Consumer<ItemStack> appender) {
        appender.accept(entity.getPickResult());
        if (!(entity instanceof AbstractMinecartContainer minecart)) return;
        for (int i = 0; i < minecart.getContainerSize(); i++) {
            ItemStack item = minecart.getItem(i);
            if (!item.isEmpty()) {
                appender.accept(item);
            }
        }
    }


}
