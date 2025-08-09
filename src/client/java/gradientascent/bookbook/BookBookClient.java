package gradientascent.bookbook;

import gradientascent.bookbook.screenhandler.ScreenHandlerTypeInit;
import gradientascent.bookbook.screen.SunderingTableScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class BookBookClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HandledScreens.register(ScreenHandlerTypeInit.SUNDERING_TABLE_SCREEN_TYPE, SunderingTableScreen::new);
	}
}