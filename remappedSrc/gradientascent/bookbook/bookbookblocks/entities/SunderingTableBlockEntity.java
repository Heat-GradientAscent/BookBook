package gradientascent.bookbook.bookbookblocks.entities;

import gradientascent.bookbook.BookBook;
import gradientascent.bookbook.bookbookblocks.BlockEntityTypeInit;
import gradientascent.bookbook.network.BlockPosPayload;
import gradientascent.bookbook.screenhandler.SunderingTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.NbtWriteView;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class SunderingTableBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPosPayload>, NamedScreenHandlerFactory {
    private int counter;

    public SunderingTableBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypeInit.SUNDERING_TABLE_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void writeFullData(WriteView view) {
        super.writeFullData(view);
        view.get(BookBook.MOD_ID).putInt("counter", this.counter);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        var bookbookData = view.getReadView(BookBook.MOD_ID);
        this.counter = bookbookData.getInt("counter", this.counter);
    }

    @Override
    public BlockPosPayload getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return new BlockPosPayload(this.pos);
    }

    @Override
    public Text getDisplayName() { return Text.of("Sunder"); }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new SunderingTableScreenHandler(syncId, playerInventory, new BlockPosPayload(getPos()));
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbt = super.toInitialChunkDataNbt(registryLookup);
        nbt.putInt("counter", this.counter);
        return nbt;
    }
}
