package gradientascent.bookbook.screenhandler;

import gradientascent.bookbook.bookbooksounds.SoundEventsInit;
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
import net.minecraft.sound.SoundCategory;

import java.util.*;


public class SunderingTableScreenHandler extends ScreenHandler {

    private PlayerEntity player;
    private ScreenHandlerContext context;
    public PropertyDelegate propertyDelegate;
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
    private final boolean[] hasPlayedSound = {false};

    // client constructor
    public SunderingTableScreenHandler(int syncId, PlayerInventory playerInventory, BlockPosPayload payload) {
        this(syncId, playerInventory, (SunderingTableBlockEntity) playerInventory.player.getWorld().getBlockEntity(payload.pos()));
        var world = playerInventory.player.getWorld();
        var pos = payload.pos();
        this.player = playerInventory.player;
        this.context = ScreenHandlerContext.create(world, pos);
        this.propertyDelegate = new ArrayPropertyDelegate(2);
        this.addProperties(this.propertyDelegate);
    }

    // server constructor (main)
    public SunderingTableScreenHandler(int syncId, PlayerInventory playerInventory, SunderingTableBlockEntity blockEntity) {
        super(ScreenHandlerTypeInit.SUNDERING_TABLE_SCREEN_TYPE, syncId);
        this.player = playerInventory.player;
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
                return (stack.hasEnchantments() || stack.isOf(Items.ENCHANTED_BOOK))
                        && !stack.isOf(Items.ENCHANTED_GOLDEN_APPLE);
            }
            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                super.onTakeItem(player, stack);
                updateOutputSlots();
            }
        });
        this.addSlot(new Slot(input, BOOK_SLOT, 51, 36) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.BOOK);
            }
            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                super.onTakeItem(player, stack);
                updateOutputSlots();
            }
        });
    }

    private void addOutputSlots() {
        this.addSlot(new Slot(output, ITEM_SLOT, 115, 36) {
            @Override public boolean canInsert(ItemStack stack) { return false; }
            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                super.onTakeItem(player, stack);
                clearInputSlots();
                updateOutputSlots();
                playSunderSound();
            }
        });
        this.addSlot(new Slot(output, BOOK_SLOT, 151, 36) {
            @Override public boolean canInsert(ItemStack stack) { return false; }
            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                super.onTakeItem(player, stack);
                clearInputSlots();
                updateOutputSlots();
                playSunderSound();
            }
        });
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if (player.getWorld().isClient) {
            return;
        }
        boolean oneOutputEmptyButNotBoth = this.output.getStack(0).isEmpty() != this.output.getStack(1).isEmpty();
        if (oneOutputEmptyButNotBoth) {
            for (int i = 0; i < this.input.size(); i++) {
                ItemStack stack = this.input.getStack(i);
                if (!stack.isEmpty()) {
                    boolean wentToPlayer = player.giveItemStack(stack);
                    if (!wentToPlayer) {
                        player.dropItem(stack, false);
                    }
                    this.input.setStack(i, ItemStack.EMPTY);
                }
            }
            for (int i = 0; i < this.output.size(); i++) {
                ItemStack stack = this.output.getStack(i);
                if (!stack.isEmpty()) {
                    boolean wentToPlayer = player.giveItemStack(stack);
                    if (!wentToPlayer) {
                        player.dropItem(stack, false);
                    }
                    this.output.setStack(i, ItemStack.EMPTY);
                }
            }
        } else {
            for (int i = 0; i < this.input.size(); i++) {
                ItemStack stack = this.input.getStack(i);
                if (!stack.isEmpty()) {
                    boolean wentToPlayer = player.giveItemStack(stack);
                    if (!wentToPlayer) {
                        player.dropItem(stack, false);
                    }
                    this.input.setStack(i, ItemStack.EMPTY);
                }
            }
            for (int i = 0; i < this.output.size(); i++) {
                this.output.setStack(i, ItemStack.EMPTY);
            }
        }
    }

    private void clearInputSlots() {
        if (output.getStack(0).isEmpty() || output.getStack(1).isEmpty()) return;
        if (this.player.experienceLevel < calculateEnchantmentCost(input.getStack(0)) && !this.player.isInCreativeMode()) return;
        acceptLevelCost();
        this.input.markDirty();
        this.input.getStack(0).decrement(1);
        this.input.getStack(1).decrement(1);
    }

    private void acceptLevelCost() {
        ItemStack itemStack = input.getStack(0);
        int totalCost = calculateEnchantmentCost(itemStack);
        if (this.player.experienceLevel >= totalCost) {
            this.player.addExperienceLevels(-totalCost);
        }
        hasConsumed = true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasStack()) return ItemStack.EMPTY;
        ItemStack originalStack = slot.getStack();
        ItemStack copy = originalStack.copy();
        int inputStart = 0;
        int outputStart = 2;
        int playerInvStart = 4;
        int playerInvEnd = this.slots.size() - 1;

        if (index >= playerInvStart) {
            if ((originalStack.hasEnchantments() || originalStack.isOf(Items.ENCHANTED_BOOK)) &&
                    !originalStack.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
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
        }
        else if (index >= outputStart) {
            ItemStack in = this.input.getStack(0);
            int cost = calculateEnchantmentCost(in);
            if (!this.player.isInCreativeMode() && this.player.experienceLevel < cost) return ItemStack.EMPTY;
            if (!this.insertItem(originalStack, playerInvStart, playerInvEnd + 1, true)) {
                return ItemStack.EMPTY;
            }
            playSunderSound();
            consumeInputsForOperation();
        }
        else {
            if (!this.insertItem(originalStack, playerInvStart, playerInvEnd + 1, true)) {
                return ItemStack.EMPTY;
            }
        }
        if (originalStack.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
        } else {
            slot.markDirty();
        }
        updateSunderCost();
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
        boolean isOutputClick = (slotIndex == 2 || slotIndex == 3) &&
                (actionType == SlotActionType.PICKUP || actionType == SlotActionType.THROW);
        super.onSlotClick(slotIndex, button, actionType, player);
        if (isOutputClick) {
            boolean notEnoughEnchants = false;
            ItemEnchantmentsComponent enchantments;
            ItemStack inputItemStack = input.getStack(0);
            if (inputItemStack.isOf(Items.ENCHANTED_BOOK)) {
                enchantments = inputItemStack.get(DataComponentTypes.STORED_ENCHANTMENTS);
            } else {
                enchantments = inputItemStack.get(DataComponentTypes.ENCHANTMENTS);
            }
            if (enchantments != null) {
                if (enchantments.getEnchantmentEntries().size() < 2 && inputItemStack.isOf(Items.ENCHANTED_BOOK)) {
                    this.propertyDelegate.set(0, 0);
                    notEnoughEnchants = true;
                }
            }
            if ((updateSunderCost() == 0)
                || (input.getStack(0).isEmpty() || input.getStack(1).isEmpty())
                || (notEnoughEnchants)
                || (this.player.experienceLevel < calculateEnchantmentCost(inputItemStack) && !this.player.isInCreativeMode())
            ) {
                return;
            }
            consumeInputsForOperation();
            updateSunderCost();
            return;
        }
        if ((slotIndex == 0 || slotIndex == 1) && actionType == SlotActionType.PICKUP) {
            updateSunderCost();
            updateOutputSlots();
            hasConsumed = false;
        }
    }

    boolean hasConsumed = false;
    private void updateOutputSlots() {
        ItemStack inputItemStack = this.input.getStack(0);
        ItemStack inputBookStack = this.input.getStack(1);
        ItemStack outputItemStack = this.output.getStack(0);
        ItemStack outputBookStack = this.output.getStack(1);
        // mid-transaction, always escape first
        boolean oneOutputEmptyButNotBoth = outputItemStack.isEmpty() != outputBookStack.isEmpty();
        if (oneOutputEmptyButNotBoth) return;
        // no possible transaction, escape second plus reset outputs
        if (inputItemStack.isEmpty() || inputBookStack.isEmpty()) {
            output.removeStack(0);
            output.removeStack(1);
            return;
        }
        // too expensive, escape third
        if (updateSunderCost() == 0) {
            return;
        }
        // if input item has only 1 enchant
        ItemEnchantmentsComponent enchantments;
        if (inputItemStack.isOf(Items.ENCHANTED_BOOK)) {
            enchantments = inputItemStack.get(DataComponentTypes.STORED_ENCHANTMENTS);
        } else {
            enchantments = inputItemStack.get(DataComponentTypes.ENCHANTMENTS);
        }
        if (enchantments != null && enchantments.getEnchantmentEntries().size() < 2 && inputItemStack.isOf(Items.ENCHANTED_BOOK)) {
            this.propertyDelegate.set(0, 0);
            return;
        }
        if (this.player.experienceLevel < calculateEnchantmentCost(inputItemStack) && !this.player.isInCreativeMode()) {
            return;
        }

        boolean isInputBook = inputItemStack.isOf(Items.ENCHANTED_BOOK);
        ItemStack outputItem;
        if (isInputBook) {
            outputItem = new ItemStack(Items.ENCHANTED_BOOK, 1);
        } else {
            ItemStack freshStackOfInput = new ItemStack(inputItemStack.getItem(), 1);
            ItemEnchantmentsComponent freshItemEnchantments = new ItemStack(freshStackOfInput.getItem()).get(DataComponentTypes.ENCHANTMENTS);
            outputItem = inputItemStack.copy();
            outputItem.set(DataComponentTypes.ENCHANTMENTS, freshItemEnchantments);
        }
        ItemStack outputBook = new ItemStack(Items.ENCHANTED_BOOK, 1);

        assert enchantments != null;
        List<Object2IntMap.Entry<RegistryEntry<Enchantment>>> entries = new ArrayList<>(enchantments.getEnchantmentEntries());
        int half = entries.size() / 2;
        ItemEnchantmentsComponent.Builder itemEnchantmentsBuilder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
        ItemEnchantmentsComponent.Builder bookEnchantmentsBuilder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
        for (int i = 0; i < half; i++) {
            Object2IntMap.Entry<RegistryEntry<Enchantment>> entry = entries.get(i);
            itemEnchantmentsBuilder.add(entry.getKey(), entry.getIntValue());
        }
        for (int i = half; i < entries.size(); i++) {
            Object2IntMap.Entry<RegistryEntry<Enchantment>> entry = entries.get(i);
            bookEnchantmentsBuilder.add(entry.getKey(), entry.getIntValue());
        }
        if (isInputBook) {
            outputItem.set(DataComponentTypes.STORED_ENCHANTMENTS, itemEnchantmentsBuilder.build());
        } else {
            outputItem.set(DataComponentTypes.ENCHANTMENTS, itemEnchantmentsBuilder.build());
        }
        outputBook.set(DataComponentTypes.STORED_ENCHANTMENTS, bookEnchantmentsBuilder.build());

        updateSunderCost();
        this.output.setStack(0, outputItem);
        this.output.setStack(1, outputBook);
    }

    private int calculateEnchantmentCost(ItemStack itemStack) {
        ItemEnchantmentsComponent enchantments;
        if (itemStack.isOf(Items.ENCHANTED_BOOK)) {
            enchantments = itemStack.get(DataComponentTypes.STORED_ENCHANTMENTS);
        } else {
            enchantments = itemStack.get(DataComponentTypes.ENCHANTMENTS);
        }
        if (enchantments == null || enchantments.isEmpty()) return 0;

        int cost = 0;
        for (var entry : enchantments.getEnchantmentEntries()) {
            Enchantment enchantment = entry.getKey().value();
            int level = entry.getIntValue();
            cost += 1 + level + (enchantmentCostWeight(enchantment) / 2);
        }
        return cost;
    }

    private void decrementStack(Inventory inv, int slot) {
        ItemStack s = inv.getStack(slot);
        if (s.isEmpty()) return;
        int newCount = s.getCount() - 1;
        if (newCount <= 0) inv.setStack(slot, ItemStack.EMPTY);
        else s.setCount(newCount);
    }

    private int enchantmentCostWeight(Enchantment enchantment) {
        return enchantment.getWeight();
    }

    private void consumeInputsForOperation() {
        ItemStack in = this.input.getStack(0);
        int cost = calculateEnchantmentCost(in);

        if (!this.player.isInCreativeMode() && this.player.experienceLevel < cost) return;
        if (!this.player.isInCreativeMode() && cost > 0) {
            this.player.addExperienceLevels(-cost);
        }
        updateSunderCost();

        decrementStack(this.input, 0);
        decrementStack(this.input, 1);
        this.input.markDirty();
        this.hasConsumed = true;
    }

    private int updateSunderCost() {
        ItemStack inputItemStack = this.input.getStack(0);
        ItemStack bookItemStack = this.input.getStack(1);
        int currentEnchantmentCost = 0;
        ItemEnchantmentsComponent enchantments;
        if (inputItemStack.isOf(Items.ENCHANTED_BOOK)) {
            enchantments = inputItemStack.get(DataComponentTypes.STORED_ENCHANTMENTS);
        } else {
            enchantments = inputItemStack.get(DataComponentTypes.ENCHANTMENTS);
        }
        if (enchantments != null && enchantments.getEnchantmentEntries().size() < 2 && inputItemStack.isOf(Items.ENCHANTED_BOOK)) {
            this.propertyDelegate.set(0, 0);
            return currentEnchantmentCost;
        }
        if (inputItemStack.isEmpty() || bookItemStack.isEmpty()) {
            this.propertyDelegate.set(0, currentEnchantmentCost);
            return currentEnchantmentCost;
        }
        int cost = calculateEnchantmentCost(inputItemStack);
        currentEnchantmentCost = Math.max(cost, 0);
        this.propertyDelegate.set(0, currentEnchantmentCost);
        return currentEnchantmentCost;
    }

    private void playSunderSound() {
        if (!hasPlayedSound[0]) {
            player.getWorld().playSound(
                null,
                player.getBlockPos(),
                SoundEventsInit.SUNDERING_TABLE_SUNDERS,
                SoundCategory.BLOCKS,
                1.0F,
                1.0F
            );
            hasPlayedSound[0] = true;
        } else {
            hasPlayedSound[0] = false;
        }
    }
}

