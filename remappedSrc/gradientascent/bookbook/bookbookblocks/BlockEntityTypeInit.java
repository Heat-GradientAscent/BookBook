package gradientascent.bookbook.bookbookblocks;

import gradientascent.bookbook.BookBook;
import gradientascent.bookbook.bookbookblocks.entities.SunderingTableBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BlockEntityTypeInit {
    public static final BlockEntityType<SunderingTableBlockEntity> SUNDERING_TABLE_BLOCK_ENTITY = register(
        "sunder_table_entity",
        FabricBlockEntityTypeBuilder.create(SunderingTableBlockEntity::new, BookBookBlocks.SUNDERING_TABLE).build()
    );

    public static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, BookBook.id(name), type);
    }

    public static void initialize() {}
}
