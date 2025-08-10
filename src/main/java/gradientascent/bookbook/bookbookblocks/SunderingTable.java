package gradientascent.bookbook.bookbookblocks;

import gradientascent.bookbook.bookbookblocks.entities.SunderingTableBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SunderingTable extends Block implements BlockEntityProvider {
    public SunderingTable(Settings settings) {
        super(
            settings
                .sounds(BlockSoundGroup.DEEPSLATE)
                .luminance((BlockState state) -> BookBookBlocks.getLuminance())
                .strength(3.0f, 1200.0f).requiresTool()
                .allowsSpawning((state, world, pos, type) -> BookBookBlocks.getSpawning())
        );
        setDefaultState(getDefaultState().with(IN_USE, false));
    }

    public static final BooleanProperty IN_USE = BooleanProperty.of("activated");

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(IN_USE);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof SunderingTableBlockEntity sunderingTableBlockEntity && player != null) {
                player.openHandledScreen(sunderingTableBlockEntity);
                world.setBlockState(pos, state.with(IN_USE, true));
                world.setBlockState(pos, state.with(IN_USE, false));
            }
        }
        return ActionResult.success(world.isClient);
    }

    // implement later for all interactions
    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        // From [0,0,0] to [16,12,16] in pixels -> in Minecraft units that's [0,0,0] to [1, 0.75, 1]
        return Block.createCuboidShape(0, 0, 0, 16, 12, 16);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        // Usually same as outline
        return getOutlineShape(state, view, pos, context);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityTypeInit.SUNDERING_TABLE_BLOCK_ENTITY.instantiate(pos, state);
    }
}
