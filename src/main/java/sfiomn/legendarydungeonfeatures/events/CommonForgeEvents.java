package sfiomn.legendarydungeonfeatures.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarydungeonfeatures.LegendaryDungeonFeatures;
import sfiomn.legendarydungeonfeatures.blocks.AbstractDungeonHeartBlock;
import sfiomn.legendarydungeonfeatures.blocks.AbstractGateBlock;
import sfiomn.legendarydungeonfeatures.config.Config;
import sfiomn.legendarydungeonfeatures.registry.BlockRegistry;
import sfiomn.legendarydungeonfeatures.registry.EffectRegistry;

@Mod.EventBusSubscriber(modid = LegendaryDungeonFeatures.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    @SubscribeEvent
    public static void onBreakingBlockIronOnCoal(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();

        if (!player.isCreative() && player.hasEffect(EffectRegistry.DUNGEON_HEART.get())) {
            BlockState state = event.getLevel().getBlockState(event.getPos());
            if (state.getBlock() != BlockRegistry.FOREST_DUNGEON_HEART_BLOCK.get() || !Config.Baked.forestDungeonHeartActiveBreakable)
                event.setCanceled(true);
        }

        BlockPos basePos = null;
        BlockPos pos = event.getPos();
        if (!player.isCreative() && event.getLevel().isClientSide) {
            Block clickedBlock = event.getLevel().getBlockState(event.getPos()).getBlock();
            if (clickedBlock instanceof AbstractGateBlock && ((AbstractGateBlock) clickedBlock).isBreakable()) {
                BlockState state = event.getLevel().getBlockState(event.getPos());
                basePos = ((AbstractGateBlock) state.getBlock()).getBasePos(state, pos);
            } else if (clickedBlock instanceof AbstractDungeonHeartBlock && ((AbstractDungeonHeartBlock) clickedBlock).isBreakable()) {
                BlockState state = event.getLevel().getBlockState(event.getPos());
                basePos = ((AbstractDungeonHeartBlock) state.getBlock()).getBasePos(state, pos);
            }
        }

        if (basePos != null) {
            if (basePos != pos && Minecraft.getInstance().gameMode != null) {
                event.setCanceled(true);
                MultiPlayerGameMode playerController = Minecraft.getInstance().gameMode;
                if (!playerController.isDestroying()) {
                    playerController.startDestroyBlock(basePos, player.getDirection());
                } else {
                    Minecraft.getInstance().gameMode.continueDestroyBlock(basePos, player.getDirection());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && !((Player) entity).isCreative() && ((Player) entity).hasEffect(EffectRegistry.DUNGEON_HEART.get())) {
            if (event.getLevel() instanceof Level) {
                event.getLevel().removeBlock(event.getPos(), true);
                ItemEntity removedBlockItem = new ItemEntity(( (Level) event.getLevel()),
                        event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(),
                        new ItemStack(event.getPlacedBlock().getBlock().asItem()));
                event.getLevel().addFreshEntity(removedBlockItem);
            }
        }
    }

    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        Player entity = event.getEntity();
        if (!entity.isCreative() && entity.hasEffect(EffectRegistry.DUNGEON_HEART.get())) {
            ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(event.getItemStack().getItem());
            if (itemRegistryName != null &&
                    Config.Baked.dungeonHeartItemsBlocked.contains(itemRegistryName.toString()))
                event.setCanceled(true);
        }
    }
}
