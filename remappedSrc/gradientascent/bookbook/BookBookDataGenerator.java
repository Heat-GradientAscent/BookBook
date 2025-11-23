package gradientascent.bookbook;

import gradientascent.bookbook.data.provider.BookBookBlockLootTableProvider;
import gradientascent.bookbook.data.provider.BookBookBlockTagProvider;
import gradientascent.bookbook.data.provider.BookBookRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class BookBookDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(BookBookBlockLootTableProvider::new);
        pack.addProvider(BookBookBlockTagProvider::new);
        pack.addProvider(BookBookRecipeProvider::new);
    }
}
