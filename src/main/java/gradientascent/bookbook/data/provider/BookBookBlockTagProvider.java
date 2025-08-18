package gradientascent.bookbook.data.provider;

import gradientascent.bookbook.bookbookblocks.BookBookBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class BookBookBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public BookBookBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
//        getTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL).addTag(Identifier.of(BookBookBlocks.SUNDERING_TABLE.toString()));
//        getTagBuilder(BlockTags.PICKAXE_MINEABLE).addTag(Identifier.of(BookBookBlocks.SUNDERING_TABLE.toString()));
        valueLookupBuilder(BlockTags.NEEDS_DIAMOND_TOOL).add(BookBookBlocks.SUNDERING_TABLE);
        valueLookupBuilder(BlockTags.PICKAXE_MINEABLE).add(BookBookBlocks.SUNDERING_TABLE);
    }
}
