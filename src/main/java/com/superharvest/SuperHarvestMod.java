package com.superharvest;

import com.superharvest.chain.ChainProcessor;
import com.superharvest.command.SuperHarvestCommand;
import com.superharvest.config.ModConfig;
import com.superharvest.event.HarvestEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * SuperHarvest — chain harvest for crops, ores, trees, and custom blocks.
 * Author: 来自太空的小头脑 (lztkdxtn@qq.com).
 * Inspired by Bukkit plugin SuperHarvest (Dru_TNT).
 */
@Mod(SuperHarvestMod.MOD_ID)
public final class SuperHarvestMod {
    public static final String MOD_ID = "superharvest";
    public static final Logger LOGGER = LogManager.getLogger("SuperHarvest");

    public SuperHarvestMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.COMMON_SPEC);
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(
                        () -> NetworkConstants.IGNORESERVERONLY,
                        (remote, isServer) -> true));

        MinecraftForge.EVENT_BUS.register(new HarvestEvents());
        MinecraftForge.EVENT_BUS.addListener(ChainProcessor::onServerTick);
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);

        // Catalogue / Configured / ModMenu-style config screen
        ModLoadingContext.get().registerExtensionPoint(
                net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) -> new com.superharvest.client.SuperHarvestConfigScreen(parent)));

        LOGGER.info("SuperHarvest loaded — author 来自太空的小头脑 (lztkdxtn@qq.com)");
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        SuperHarvestCommand.register(event.getDispatcher());
    }
}
