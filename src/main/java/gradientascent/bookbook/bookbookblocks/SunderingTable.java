package gradientascent.bookbook.bookbookblocks;

import gradientascent.bookbook.bookbookblocks.entities.SunderingTableBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SunderingTable extends Block implements BlockEntityProvider {
    public static final BooleanProperty IN_USE = BooleanProperty.of("activated");
    private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 12, 16);

    public static int getLuminance() { return 6; }

    public static boolean getSpawning() { return false; }

    public SunderingTable(Settings settings) {
        super(
            settings
                .nonOpaque()
                .luminance(s -> getLuminance())
                .allowsSpawning((s, w, p, t) -> getSpawning())
                .sounds(BlockSoundGroup.DEEPSLATE)
                .strength(3.0f, 1200.0f).requiresTool()
        );
        setDefaultState(getDefaultState().with(IN_USE, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(IN_USE);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof SunderingTableBlockEntity sunderingTableBlockEntity && player != null) {
                player.openHandledScreen(sunderingTableBlockEntity);
                world.setBlockState(pos, state.with(IN_USE, true));
                world.setBlockState(pos, state.with(IN_USE, false));
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return getOutlineShape(state, view, pos, context);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityTypeInit.SUNDERING_TABLE_BLOCK_ENTITY.instantiate(pos, state);
    }
}
