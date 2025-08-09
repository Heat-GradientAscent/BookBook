package gradientascent.bookbook.screenhandler;

import gradientascent.bookbook.BookBook;
import gradientascent.bookbook.network.BlockPosPayload;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlerTypeInit {
    public static final ScreenHandlerType<SunderingTableScreenHandler> SUNDERING_TABLE_SCREEN_TYPE =
            register(
                "sundering_table",
                    SunderingTableScreenHandler::new,
                    BlockPosPayload.PACKET_CODEC
            );

    public static <T extends ScreenHandler, P extends CustomPayload> ExtendedScreenHandlerType<T, P>
    register(String name, ExtendedScreenHandlerType.ExtendedFactory<T, P> factory, PacketCodec<? super RegistryByteBuf, P> codec) {
        return Registry.register(Registries.SCREEN_HANDLER, BookBook.id(name), new ExtendedScreenHandlerType<>(factory, codec));
    }

    public static void initialize() {}
}
