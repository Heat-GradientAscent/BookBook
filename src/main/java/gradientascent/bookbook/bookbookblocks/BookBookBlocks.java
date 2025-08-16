package gradientascent.bookbook.bookbookblocks;

import gradientascent.bookbook.BookBook;
import gradientascent.bookbook.bookbookitems.BookBookItems;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;


public class BookBookBlocks {
    public static final Block SUNDERING_TABLE;

    static {
        Identifier id = BookBook.id("sundering_table");
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);

        AbstractBlock.Settings blockSettings = AbstractBlock.Settings.copy(Blocks.DEEPSLATE)
                .registryKey(blockKey);

        SUNDERING_TABLE = Registry.register(Registries.BLOCK, blockKey, new SunderingTable(blockSettings));

        Item.Settings itemSettings = new Item.Settings()
                .useBlockPrefixedTranslationKey()
                .registryKey(itemKey);
        Registry.register(Registries.ITEM, itemKey, new BlockItem(SUNDERING_TABLE, itemSettings));
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(BookBookItems.CUSTOM_ITEM_GROUP_KEY).register(
                entries -> entries.add(SUNDERING_TABLE.asItem())
        );
    }
}