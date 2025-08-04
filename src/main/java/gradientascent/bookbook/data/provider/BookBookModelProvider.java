package gradientascent.bookbook.data.provider;

import gradientascent.bookbook.bookbookblocks.BookBookBlocks;
import gradientascent.bookbook.bookbookitems.BookBookItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

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
        itemModelGenerator.register(BookBookItems.SUNDERING_TABLE, Models.CUBE_DIRECTIONAL);
    }
}
