package gradientascent.bookbook.bookbookblocks;

import gradientascent.bookbook.BookBook;
import gradientascent.bookbook.bookbookitems.BookBookItems;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class BookBookBlocks {
    public static Block register(Block block, String name, boolean shouldRegisterItem) {
        Identifier id = BookBook.id(name);
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, id, blockItem);
        }
        return Registry.register(Registries.BLOCK, id, block);
    }

    public static int getLuminance() { return 6; }

    public static boolean getSpawning() {
        return false;
    }

    public static final Block SUNDERING_TABLE = register(
        new SunderingTable(AbstractBlock.Settings.create()),
        "sundering_table",
        true
    );

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(BookBookItems.CUSTOM_ITEM_GROUP_KEY).register(
                (itemGroup) -> itemGroup.add(BookBookBlocks.SUNDERING_TABLE.asItem()
            )
        );
    }
}