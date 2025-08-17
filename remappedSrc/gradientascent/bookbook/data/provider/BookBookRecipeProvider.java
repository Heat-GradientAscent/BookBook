package gradientascent.bookbook.data.provider;

import gradientascent.bookbook.bookbookblocks.BookBookBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeGenerator;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class BookBookRecipeProvider extends FabricRecipeProvider {
    public BookBookRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        return new RecipeGenerator(registries, exporter) {
            @Override
            public void generate() {
                RegistryEntryLookup<Item> itemLookup = registries.getOrThrow(RegistryKeys.ITEM);
                ShapedRecipeJsonBuilder.create(
                        itemLookup,
                        RecipeCategory.MISC,
                        BookBookBlocks.SUNDERING_TABLE.asItem(),
                        1
                    )
                    .pattern(" # ")
                    .pattern("nOn")
                    .pattern("ODO")
                    .input('#', Items.BLUE_CARPET)
                    .input('n', Items.NETHERITE_INGOT)
                    .input('O', Items.OBSIDIAN)
                    .input('D', Items.DEEPSLATE)
                    .criterion(hasItem(Items.BLUE_CARPET), conditionsFromItem(Items.BLUE_CARPET))
                    .offerTo(exporter);
            }
        };
    }

    @Override
    public String getName() {
        return "BookBook Recipes";
    }
}
