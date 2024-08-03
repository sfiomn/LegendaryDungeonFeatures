package sfiomn.legendarydungeonfeatures;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sfiomn.legendarydungeonfeatures.config.Config;
import sfiomn.legendarydungeonfeatures.entities.render.DesertKeyRenderer;
import sfiomn.legendarydungeonfeatures.entities.render.ForestKeyRenderer;
import sfiomn.legendarydungeonfeatures.blockentities.render.ForestDungeonHeartRenderer;
import sfiomn.legendarydungeonfeatures.network.NetworkHandler;
import sfiomn.legendarydungeonfeatures.registry.*;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(LegendaryDungeonFeatures.MOD_ID)
public class LegendaryDungeonFeatures
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "legendarydungeonfeatures";
    public static Path configPath = FMLPaths.CONFIGDIR.get();

    // modConfigPath used to create a config directory if necessary
    public static Path modConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "legendarydungeonfeatures");

    public LegendaryDungeonFeatures() {

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        // Register the setup method for modloading
        modBus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        modBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        modBus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        modBus.addListener(this::doClientStuff);

        BlockRegistry.register(modBus);
        EffectRegistry.register(modBus);
        EntityTypeRegistry.register(modBus);
        ItemRegistry.register(modBus);
        SoundRegistry.register(modBus);
        BlockEntityRegistry.register(modBus);

        Config.register();

        forgeBus.addListener(this::reloadJsonConfig);

        // Register ourselves for server and other game events we are interested in
        forgeBus.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        Config.Baked.bakeCommon();

        NetworkHandler.register();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {

        event.enqueueWork(() ->
        {
            RenderTypeLookup.setRenderLayer(BlockRegistry.MEAT_RACK_BLOCK.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BlockRegistry.HONEY_POND_BLOCK.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BlockRegistry.OBELISK_BLOCK.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BlockRegistry.CLOVER_PATCH_BLOCK.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BlockRegistry.GLOWING_BULB_BLOCK.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BlockRegistry.MOSS_BLOCK.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BlockRegistry.CAPTAIN_CHAIR_BLOCK.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BlockRegistry.CAPTAIN_CHAIR_TOP_BLOCK.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BlockRegistry.TRIBAL_TORCH_BLOCK.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BlockRegistry.TRIBAL_TORCH_WALL_BLOCK.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BlockRegistry.HIVE_LANTERN_BLOCK.get(), RenderType.cutout());

            RenderTypeLookup.setRenderLayer(BlockRegistry.ACACIA_WINDOW_PANE.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(BlockRegistry.ACACIA_WINDOW_BLOCK.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(BlockRegistry.BIRCH_WINDOW_PANE.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(BlockRegistry.BIRCH_WINDOW_BLOCK.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(BlockRegistry.DARK_OAK_WINDOW_PANE.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(BlockRegistry.DARK_OAK_WINDOW_BLOCK.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(BlockRegistry.JUNGLE_WINDOW_PANE.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(BlockRegistry.JUNGLE_WINDOW_BLOCK.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(BlockRegistry.OAK_WINDOW_PANE.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(BlockRegistry.OAK_WINDOW_BLOCK.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(BlockRegistry.SPRUCE_WINDOW_PANE.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(BlockRegistry.SPRUCE_WINDOW_BLOCK.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(BlockRegistry.CRIMSON_WINDOW_PANE.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(BlockRegistry.CRIMSON_WINDOW_BLOCK.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BlockRegistry.ORNATE_IRON_WINDOW_PANE.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(BlockRegistry.ORNATE_IRON_WINDOW_BLOCK.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BlockRegistry.WARPED_WINDOW_PANE.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(BlockRegistry.WARPED_WINDOW_BLOCK.get(), RenderType.cutout());

            RenderTypeLookup.setRenderLayer(BlockRegistry.FOREST_DUNGEON_GATE_BLOCK.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BlockRegistry.FOREST_DUNGEON_HEART_BLOCK.get(), RenderType.cutout());

            RenderTypeLookup.setRenderLayer(BlockRegistry.SPIDER_EGGS_BLOCK.get(), RenderType.cutout());
        });

        DistExecutor.safeRunWhenOn(Dist.CLIENT, LegendaryDungeonFeatures::registerEntityRendering);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, LegendaryDungeonFeatures::registerTileEntityRenderer);
    }

    private static DistExecutor.SafeRunnable registerEntityRendering() {

        return new DistExecutor.SafeRunnable()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void run()
            {
                RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.SEAT_ENTITY.get(), SeatRenderer::new);

                RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.TINY_XP_BOTTLE_ENTITY.get(), renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer()));
                RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.COMMON_XP_BOTTLE_ENTITY.get(), renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer()));
                RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.RARE_XP_BOTTLE_ENTITY.get(), renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer()));
                RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.EPIC_XP_BOTTLE_ENTITY.get(), renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer()));
                RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.LEGENDARY_XP_BOTTLE_ENTITY.get(), renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer()));

                RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.FOREST_KEY_ENTITY.get(), ForestKeyRenderer::new);
                RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.DESERT_KEY_ENTITY.get(), DesertKeyRenderer::new);
            }
        };
    }

    private static DistExecutor.SafeRunnable registerTileEntityRenderer() {

        return new DistExecutor.SafeRunnable()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void run()
            {
                ClientRegistry.bindTileEntityRenderer(BlockEntityRegistry.OBELISK_BLOCK_ENTITY.get(), ObeliskRenderer::new);
                ClientRegistry.bindTileEntityRenderer(BlockEntityRegistry.FOREST_DUNGEON_GATE_BLOCK_ENTITY.get(), ForestDungeonGateRendererOld::new);
                ClientRegistry.bindTileEntityRenderer(BlockEntityRegistry.FOREST_DUNGEON_HEART_BLOCK_ENTITY.get(), ForestDungeonHeartRenderer::new);
            }
        };
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }

    private void reloadJsonConfig(final AddReloadListenerEvent event)
    {
        event.addListener(new ReloadListener<Void>()
              {
                  @Nonnull
                  @ParametersAreNonnullByDefault
                  @Override
                  protected Void prepare(IResourceManager manager, IProfiler profiler)
                  {
                      return null;
                  }

                  @ParametersAreNonnullByDefault
                  @Override
                  protected void apply(Void objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn)
                  {
                      Config.Baked.bakeCommon();
                  }

              }
        );
    }
}
