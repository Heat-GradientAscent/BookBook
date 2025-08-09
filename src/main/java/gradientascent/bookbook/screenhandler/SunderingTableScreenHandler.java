package gradientascent.bookbook.screenhandler;

import gradientascent.bookbook.bookbookblocks.BookBookBlocks;
import gradientascent.bookbook.bookbookblocks.entities.SunderingTableBlockEntity;
import gradientascent.bookbook.network.BlockPosPayload;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import java.util.*;


public class SunderingTableScreenHandler extends ScreenHandler {

    private SunderingTableBlockEntity blockEntity;
    private ScreenHandlerContext context;
    private PropertyDelegate propertyDelegate;
    private boolean updating = false;
    private final Inventory input = new SimpleInventory(2) {
        @Override
        public void markDirty() {
            super.markDirty();
            if (!updating) {
                onContentChanged(this);
            }
        }
    }; // Input
    private final Inventory output = new SimpleInventory(2); // Output

    // client constructor
    public SunderingTableScreenHandler(int syncId, PlayerInventory playerInventory, BlockPosPayload payload) {
        this(syncId, playerInventory, (SunderingTableBlockEntity) playerInventory.player.getWorld().getBlockEntity(payload.pos()));
        var world = playerInventory.player.getWorld();
        var pos = payload.pos();
        this.blockEntity = (SunderingTableBlockEntity) world.getBlockEntity(pos);
        this.context = ScreenHandlerContext.create(world, pos);
        this.propertyDelegate = new ArrayPropertyDelegate(2);
        this.addProperties(this.propertyDelegate);
    }

    // server constructor (main)
    public SunderingTableScreenHandler(int syncId, PlayerInventory playerInventory, SunderingTableBlockEntity blockEntity) {
        super(ScreenHandlerTypeInit.SUNDERING_TABLE_SCREEN_TYPE, syncId);
        this.blockEntity = blockEntity;
        this.context = ScreenHandlerContext.create(blockEntity.getWorld(), blockEntity.getPos());
        this.propertyDelegate = new ArrayPropertyDelegate(2);
        this.addProperties(this.propertyDelegate);

        addInputSlots();
        addOutputSlots();

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        // Optional: level cost sync
        this.addProperties(this.propertyDelegate);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, BookBookBlocks.SUNDERING_TABLE);
    }

    private void addPlayerHotbar(PlayerInventory inv) {
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 9; i++) {
                addSlot(new Slot(inv, 9 + i + (9 * j), 8 + (18 * i), 84 + (18 * j)));
            }
        }
    }

    private void addPlayerInventory(PlayerInventory inv) {
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(inv, i, 8 + (18 * i), 142));
        }
    }

    private static final int ITEM_SLOT = 0;
    private static final int BOOK_SLOT = 1;

    private void addInputSlots() {
        this.addSlot(new Slot(input, ITEM_SLOT, 15, 36) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.hasEnchantments() && !stack.isOf(Items.ENCHANTED_GOLDEN_APPLE);
            }
        });
        this.addSlot(new Slot(input, BOOK_SLOT, 51, 36) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.BOOK);
            }
        });
    }

    private void addOutputSlots() {
        this.addSlot(new Slot(output, ITEM_SLOT, 115, 36) {
            @Override public boolean canInsert(ItemStack stack) { return false; }
            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return true; // allow taking
            }
            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                super.onTakeItem(player, stack); // keep vanilla behavior
                clearInputSlots();
                updateOutputSlots();
            }
        });
        this.addSlot(new Slot(output, BOOK_SLOT, 151, 36) {
            @Override public boolean canInsert(ItemStack stack) { return false; }
            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return true; // allow taking
            }
            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                super.onTakeItem(player, stack); // keep vanilla behavior
                clearInputSlots();
                updateOutputSlots();
            }
        });
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if (!player.getWorld().isClient) {
            this.dropInventory(player, this.input);
            for (int i = 0; i < 2; i++) {
                ItemStack out = this.output.getStack(i);
                if (!out.isEmpty()) {
                    boolean wentToPlayer = player.giveItemStack(out);
                    if (!wentToPlayer) {
                        player.dropItem(out, false);
                    }
                    this.output.setStack(i, ItemStack.EMPTY);
                }
            }
        }
    }

    private void clearInputSlots() {
        if (output.getStack(0).isEmpty() || output.getStack(1).isEmpty()) return;
        this.input.markDirty();
        this.input.getStack(0).setCount(input.getStack(0).getCount() - 1);
        this.input.getStack(1).setCount(input.getStack(1).getCount() - 1);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasStack()) return ItemStack.EMPTY;
        ItemStack originalStack = slot.getStack();
        ItemStack copy = originalStack.copy();

        int inputStart = 0;
        int outputEnd = 3;

        int playerInvStart = 4;
        int playerInvEnd = this.slots.size() - 1;

        if (index >= playerInvStart) {
            if (originalStack.hasEnchantments() && !originalStack.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
                if (!this.insertItem(originalStack, inputStart, inputStart + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (originalStack.isOf(Items.BOOK)) {
                if (!this.insertItem(originalStack, inputStart + 1, inputStart + 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                return ItemStack.EMPTY;
            }
        } else if (index <= outputEnd) {
            if (!this.insertItem(originalStack, playerInvStart, playerInvEnd + 1, true)) {
                return ItemStack.EMPTY;
            }
            clearInputSlots();
        } else {
            return ItemStack.EMPTY;
        }
        if (originalStack.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
        } else {
            slot.markDirty();
        }
        return copy;
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        if (updating) return;
        updating = true;
        try {
            updateOutputSlots();
        } finally {
            updating = false;
        }
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        if (
                (slotIndex == 2 || slotIndex == 3)
                && (actionType == SlotActionType.PICKUP || actionType == SlotActionType.THROW)
        ) {
            Slot outputSlot = this.slots.get(slotIndex);
            outputSlot.markDirty();
            clearInputSlots();
        }
        if ((slotIndex == 0 || slotIndex == 1) && actionType == SlotActionType.PICKUP) {
            updateOutputSlots();
        }
        super.onSlotClick(slotIndex, button, actionType, player);
    }

    private void updateOutputSlots() {
        if (!(output.getStack(0).isEmpty() && output.getStack(1).isEmpty())) return;
        ItemStack inputStack = this.input.getStack(0);
        ItemStack itemStack = new ItemStack(inputStack.getItem(), inputStack.getCount());
        itemStack.applyComponentsFrom(inputStack.getComponents());
//        ItemEnchantmentsComponent enchComp = itemStack.get(DataComponentTypes.ENCHANTMENTS);
//        if (enchComp != null) {
//            enchComp.getEnchantmentEntries().clear();
//        }
        ItemStack bookStack = this.input.getStack(1);
        if (inputStack.isEmpty() || bookStack.isEmpty()) {
            this.output.setStack(0, ItemStack.EMPTY);
            this.output.setStack(1, ItemStack.EMPTY);
            return;
        }
        // Copy input stacks to output slots (clone them)
        ItemStack outputStack0 = new ItemStack(itemStack.getItem(), 1);
        ItemStack outputStack1 = new ItemStack(bookStack.getItem(), 1);
        // Get enchantments from original inputStack
        ItemEnchantmentsComponent enchantments = inputStack.get(DataComponentTypes.ENCHANTMENTS);
        if (enchantments == null) return;
        // Convert enchantments set to list
        List<Object2IntMap.Entry<RegistryEntry<Enchantment>>> entries = new ArrayList<>(enchantments.getEnchantmentEntries());
        int half = entries.size() / 2;
        List<Object2IntMap.Entry<RegistryEntry<Enchantment>>> list = new ArrayList<>(entries);
        for (int i = 0; i < half; i++) {
            RegistryEntry<Enchantment> enchEntry = list.get(i).getKey();
            int level = list.get(i).getIntValue();
            outputStack0.addEnchantment(enchEntry, level);
        }
        for (int i = half; i < list.size(); i++) {
            RegistryEntry<Enchantment> enchEntry = list.get(i).getKey();
            int level = list.get(i).getIntValue();
            outputStack1.addEnchantment(enchEntry, level);
        }
        // Set output slots with modified stacks
        this.output.setStack(0, outputStack0);
        this.output.setStack(1, outputStack1);
    }
}

