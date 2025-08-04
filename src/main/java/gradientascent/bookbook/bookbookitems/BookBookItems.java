package gradientascent.bookbook.bookbookitems;

import gradientascent.bookbook.BookBook;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BookBookItems {
    public static Item register(Item item, String id) {
        Identifier itemID = Identifier.of(BookBook.MOD_ID, id);
        return Registry.register(Registries.ITEM, itemID, item);
    }

    public static final Item SUNDERING_TABLE = register(
        new Item(new Item.Settings()),
        "sundering_table"
    );

    public static final RegistryKey<ItemGroup> CUSTOM_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(BookBook.MOD_ID, "item_group"));
    public static final ItemGroup CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(BookBookItems.SUNDERING_TABLE))
            .displayName(Text.translatable("itemGroup.bookbook"))
            .build();

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, CUSTOM_ITEM_GROUP_KEY, CUSTOM_ITEM_GROUP);
    }
}
