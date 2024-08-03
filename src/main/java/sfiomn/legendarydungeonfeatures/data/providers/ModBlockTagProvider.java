package sfiomn.legendarydungeonfeatures.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendary_additions.LegendaryAdditions;
import sfiomn.legendary_additions.registry.BlockRegistry;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, LegendaryAdditions.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(BlockRegistry.CARVED_ACACIA_LOG_BLOCK.get(),
                        BlockRegistry.ANCESTRAL_WOOD.get(),
                        BlockRegistry.CARVED_BIRCH_LOG_BLOCK.get(),
                        BlockRegistry.CARVED_DARK_OAK_LOG_BLOCK.get(),
                        BlockRegistry.CARVED_OAK_LOG_BLOCK.get(),
                        BlockRegistry.CARVED_JUNGLE_LOG_BLOCK.get(),
                        BlockRegistry.CAPTAIN_CHAIR_BLOCK.get(),
                        BlockRegistry.CAPTAIN_CHAIR_TOP_BLOCK.get(),
                        BlockRegistry.FOREST_DUNGEON_GATE_BLOCK.get(),
                        BlockRegistry.FOREST_DUNGEON_HEART_BLOCK.get(),
                        BlockRegistry.HIVE_LANTERN_BLOCK.get(),
                        BlockRegistry.MEAT_RACK_BLOCK.get());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(BlockRegistry.HONEY_POND_BLOCK.get(),
                        BlockRegistry.XP_STORAGE_BLOCK.get(),
                        BlockRegistry.OBELISK_BLOCK.get());

        this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(BlockRegistry.MOSS_BLOCK.get());

        this.tag(BlockTags.TALL_FLOWERS)
                .add(BlockRegistry.GLOWING_BULB_BLOCK.get());
    }
}
