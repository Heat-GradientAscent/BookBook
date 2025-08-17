package gradientascent.bookbook.data.provider;

import gradientascent.bookbook.bookbookblocks.BookBookBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.minecraft.client.data.*;

public class BookBookModelProvider extends FabricModelProvider {
    public BookBookModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(BookBookBlocks.SUNDERING_TABLE);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(BookBookBlocks.SUNDERING_TABLE.asItem(), Models.CUBE_DIRECTIONAL);
    }
}
