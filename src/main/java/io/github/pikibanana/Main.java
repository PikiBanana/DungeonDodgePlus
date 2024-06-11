package io.github.pikibanana;

import io.github.pikibanana.chat.ChatMessageHandlerImpl;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("DungeonDodge+");

	@Override
	public void onInitialize() {
		LOGGER.info("Initiating...");
		LOGGER.info("Loading config...");
		try {
			DungeonDodgePlusConfig.register();
			DungeonDodgePlusConfig.get();
			LOGGER.info("Config loaded correctly!");
		} catch (Exception e) {
			LOGGER.warn("Config did not load correctly!");
			LOGGER.error(e.toString());
		}

		LOGGER.info("Registering chat listeners...");
		try {
			ChatMessageHandlerImpl chatHandler = new ChatMessageHandlerImpl();
			ClientReceiveMessageEvents.ALLOW_CHAT.register(chatHandler::allowReceiveChatMessage);
			ClientReceiveMessageEvents.CHAT.register(chatHandler::onReceiveChatMessage);
			ClientReceiveMessageEvents.CHAT_CANCELED.register(chatHandler::onReceiveChatMessageCanceled);
			ClientReceiveMessageEvents.MODIFY_GAME.register(chatHandler::modifyChatMessage);
		} catch (Exception e) {
			LOGGER.warn("Chat listeners did not register correctly!");
			LOGGER.error(e.toString());
		}
		LOGGER.info("DungeonDodge+ is ready!");
	}
}