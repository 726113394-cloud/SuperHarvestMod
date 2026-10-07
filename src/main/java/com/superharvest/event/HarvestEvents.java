package com.superharvest.event;

import com.superharvest.chain.ChainProcessor;
import com.superharvest.config.ModConfig;
import com.superharvest.util.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Starts chain jobs on player block breaks; cancels when tool is switched.
 */
public final class HarvestEvents {

    /** Switching main-hand tool (or empty hand) stops any running chain. */
    @SubscribeEvent
    public void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (event.getSlot() == EquipmentSlot.MAINHAND || event.getSlot() == EquipmentSlot.OFFHAND) {
            ChainProcessor.cancelPlayer(player.getUUID());
        }
    }

    @SubscribeEvent
    public void onLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ChainProcessor.cancelPlayer(player.getUUID());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.isCanceled() || !(event.getPlayer() instanceof ServerPlayer player)) {
            return;
        }
        if (!(player.level() instanceof net.minecraft.server.level.ServerLevel)) {
            return;
        }
        BlockPos pos = event.getPos();
        if (ChainProcessor.consumeChaining(pos)) {
            return;
        }
        if (ModConfig.COMMON.sneakOnly.get() && !player.isShiftKeyDown()) {
            return;
        }

        BlockState state = event.getState();
        ItemStack tool = player.getMainHandItem();

        // 1) farming / mining / logging (tool-gated)
        if (ModConfig.COMMON.enableFarming.get()
                && BlockHelper.isHoe(tool)
                && BlockHelper.isCrop(state)
                && BlockHelper.canCropHarvest(state)) {
            ChainProcessor.submit(player, ChainProcessor.Kind.FARM, pos, state);
            return;
        }
        if (ModConfig.COMMON.enableMining.get()
                && BlockHelper.isPickaxe(tool)
                && BlockHelper.isOre(state)) {
            ChainProcessor.submit(player, ChainProcessor.Kind.MINE, pos, state);
            return;
        }
        if (ModConfig.COMMON.enableLogging.get()
                && BlockHelper.isAxe(tool)
                && BlockHelper.isLogLike(state)) {
            ChainProcessor.submit(player, ChainProcessor.Kind.LOG, pos, state);
            return;
        }

        // 2) custom chainable blocks (player-defined, includes modded blocks)
        if (ModConfig.COMMON.enableCustom.get() && BlockHelper.isCustomChainable(state)) {
            if (ModConfig.COMMON.customRequirePickaxe.get() && !BlockHelper.isPickaxe(tool)) {
                return;
            }
            if (!ModConfig.COMMON.customAllowAnyTool.get()
                    && !BlockHelper.isPickaxe(tool)
                    && !BlockHelper.isAxe(tool)
                    && !BlockHelper.isHoe(tool)
                    && !tool.isEmpty()) {
                return;
            }
            ChainProcessor.submit(player, ChainProcessor.Kind.CUSTOM, pos, state);
        }
    }
}
