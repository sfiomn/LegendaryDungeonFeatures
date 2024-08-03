package sfiomn.legendarydungeonfeatures.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarydungeonfeatures.LegendaryDungeonFeatures;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, LegendaryDungeonFeatures.MOD_ID);

    public static final RegistryObject<SoundEvent> LOCK_UNLOCKED = registerSoundEvent("lock_unlocked");
    public static final RegistryObject<SoundEvent> OPEN_GATE_SUCCESSFUL = registerSoundEvent("open_gate_successful");
    public static final RegistryObject<SoundEvent> OPEN_GATE_FAILED = registerSoundEvent("open_gate_failed");


    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(
                new ResourceLocation(LegendaryDungeonFeatures.MOD_ID, name)
            ));
    }
    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
