package gradientascent.bookbook.bookbookblocks;

import net.minecraft.block.*;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static gradientascent.bookbook.BookBook.MOD_ID;

public class SunderingTable extends Block {
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
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
        if (!player.getAbilities().allowModifyWorld || state.get(IN_USE)) {
            return ActionResult.PASS;
        } else {
            // open GUI here
            world.setBlockState(pos, state.with(IN_USE, true));
            LOGGER.info("Attempted to interact, nice!");

            // MOVE THIS TO WHENEVER THE GUI CLOSES
            world.setBlockState(pos, state.with(IN_USE, false));
            return ActionResult.SUCCESS;
        }
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
}
