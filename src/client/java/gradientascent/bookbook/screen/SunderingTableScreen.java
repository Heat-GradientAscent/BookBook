package gradientascent.bookbook.screen;

import gradientascent.bookbook.BookBook;
import gradientascent.bookbook.screenhandler.SunderingTableScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SunderingTableScreen extends HandledScreen<SunderingTableScreenHandler> {
    public static final Identifier TEXTURE = BookBook.id("textures/gui/container/sundering_table.png");

    public SunderingTableScreen(SunderingTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    private String getSunderCostText() {
        int currentEnchantmentCost = this.handler.propertyDelegate.get(0);
        PlayerEntity thePlayer = MinecraftClient.getInstance().player;
        if (currentEnchantmentCost == 0 || thePlayer == null) {
            return "No-op";
        }
        boolean affordable = thePlayer.isInCreativeMode() || thePlayer.experienceLevel >= currentEnchantmentCost;

        String colorCode = affordable ? "§a" : "§c";
        return colorCode + "Sunder Cost: " + currentEnchantmentCost;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
        // Now you call the new instance method
        String costString = getSunderCostText();
        if (!costString.isEmpty()) {
            OrderedText costText = Text.of(costString).asOrderedText();
            int textWidth = this.textRenderer.getWidth(costText);
            this.textRenderer.draw(
                costText,
                (float) (this.x + 166 - textWidth),
                (float) (this.y + 58),
                0x313131,
                false,
                context.getMatrices().peek().getPositionMatrix(),
                MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers(),
                TextRenderer.TextLayerType.NORMAL,
                0,
                15728880
            );
        }
    }
}
