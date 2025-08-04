package gradientascent.bookbook.data.provider;

import gradientascent.bookbook.bookbookblocks.BookBookBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class BookBookBlockLootTableProvider extends FabricBlockLootTableProvider {
    public BookBookBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
//        addDrop(BookBookBlocks.SUNDERING_TABLE);
        addDrop(BookBookBlocks.SUNDERING_TABLE, this.drops(BookBookBlocks.SUNDERING_TABLE));
    }
}