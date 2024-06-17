package io.github.pikibanana;

import io.github.pikibanana.chat.ChatMessageHandlerImpl;
import io.github.pikibanana.config.ConfigKeybind;
import io.github.pikibanana.config.DungeonDodgePlusConfig;
import io.github.pikibanana.dungeonapi.BlessingFinderData;
import io.github.pikibanana.dungeonapi.DungeonDodgeConnection;
import io.github.pikibanana.dungeonapi.DungeonTracker;
import io.github.pikibanana.keybinds.QuickWardrobe;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("[DungeonDodge+]");

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
        LOGGER.info("Registering chat listeners and event handlers...");
        try {
            ChatMessageHandlerImpl chatHandler = new ChatMessageHandlerImpl();
            ClientReceiveMessageEvents.ALLOW_CHAT.register(chatHandler::allowReceiveChatMessage);
            ClientReceiveMessageEvents.CHAT.register(chatHandler::onReceiveChatMessage);
            ClientReceiveMessageEvents.CHAT_CANCELED.register(chatHandler::onReceiveChatMessageCanceled);

            DungeonTracker dungeonTracker = new DungeonTracker();
            ClientReceiveMessageEvents.GAME.register(dungeonTracker::handleMessage);

            DungeonDodgeConnection connectionTracker = new DungeonDodgeConnection();
            ClientReceiveMessageEvents.GAME.register(connectionTracker::handleMessage);
            ClientReceiveMessageEvents.ALLOW_GAME.register(connectionTracker::allowMessage);

//            // Adding debug log for registration
//            LOGGER.info("Registering DISCONNECT event handler...");
//            ServerPlayConnectionEvents.DISCONNECT.register(connectionTracker::handleLeave);
//            LOGGER.info("DISCONNECT event handler registered."); TODO: Fix this part
        } catch (Exception e) {
            LOGGER.warn("Event listeners did not register correctly!");
            LOGGER.error(e.toString());
        }
        BlessingFinderData.init();
        ConfigKeybind.register();
        QuickWardrobe.register();
        LOGGER.info("DungeonDodge+ is ready!");
    }
}
