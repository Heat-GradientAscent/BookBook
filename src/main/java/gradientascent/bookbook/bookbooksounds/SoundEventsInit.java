package gradientascent.bookbook.bookbooksounds;

import gradientascent.bookbook.BookBook;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundEventsInit {

    public static final SoundEvent SUNDERING_TABLE_SUNDERS = registerSoundEvent("sundering_table.sunder");

    public static SoundEvent registerSoundEvent(String id) {
        return Registry.register(Registries.SOUND_EVENT, Identifier.of(BookBook.MOD_ID, id), SoundEvent.of(Identifier.of(BookBook.MOD_ID, id)));
    }

    public static void initialize() {}
}