package gradientascent.bookbook;

import gradientascent.bookbook.bookbookblocks.BlockEntityTypeInit;
import gradientascent.bookbook.bookbookblocks.BookBookBlocks;
import gradientascent.bookbook.bookbookitems.BookBookItems;
import gradientascent.bookbook.screenhandler.ScreenHandlerTypeInit;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BookBook implements ModInitializer {
	public static final String MOD_ID = "bookbook";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		BookBookItems.initialize();
		BookBookBlocks.initialize();

		// Entities
		BlockEntityTypeInit.initialize();

		// Screenhandlers
		ScreenHandlerTypeInit.initialize();
		LOGGER.info("BookBook is loaded!");

	}
}
