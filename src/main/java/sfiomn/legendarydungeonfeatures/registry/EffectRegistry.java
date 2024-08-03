package sfiomn.legendarydungeonfeatures.registry;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarydungeonfeatures.LegendaryDungeonFeatures;
import sfiomn.legendarydungeonfeatures.effects.DungeonHeartEffect;

public class EffectRegistry
{
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, LegendaryDungeonFeatures.MOD_ID);

    public static final RegistryObject<MobEffect> DUNGEON_HEART = EFFECTS.register("dungeon_heart", DungeonHeartEffect::new);

    public static void register (IEventBus eventBus){
        EFFECTS.register(eventBus);
    }
}
