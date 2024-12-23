package io.github.pikibanana;

import io.github.pikibanana.chat.ChatMessageHandlerImpl;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.dungeonapi.BlessingFinderData;
import io.github.pikibanana.dungeonapi.DungeonDodgeConnection;
import io.github.pikibanana.dungeonapi.DungeonTracker;
import io.github.pikibanana.dungeonapi.PlayerStats;
import io.github.pikibanana.dungeonapi.essence.EssenceCounter;
import io.github.pikibanana.dungeonapi.essence.EssenceTracker;
import io.github.pikibanana.hud.FPSRenderer;
import io.github.pikibanana.keybinds.Keybinds;
import io.github.pikibanana.keybinds.QuickDungeon;
import io.github.pikibanana.keybinds.QuickWardrobe;
import io.github.pikibanana.misc.SheepRandomizer;
import io.github.pikibanana.util.UpdateChecker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("DungeonDodge+");
    public static final String MOD_ID = "dungeondodgeplus";
    public static final String MOD_VERSION = "0.6";
    public static DungeonDodgePlusConfig.Features features;

    @Override
    public void onInitialize() {
        LOGGER.info("Initiating...");
        LOGGER.info("Loading config...");
        try {
            DungeonDodgePlusConfig.register();
            DungeonDodgePlusConfig.get();
            features = DungeonDodgePlusConfig.get().features;
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
//            ClientReceiveMessageEvents.GAME.register(connectionTracker::handleMessage);
            ClientReceiveMessageEvents.ALLOW_GAME.register(connectionTracker::allowMessage);

            EssenceTracker essenceTracker = new EssenceTracker();
            ClientReceiveMessageEvents.GAME.register(essenceTracker::handleMessage);

        } catch (Exception e) {
            LOGGER.warn("Event listeners did not register correctly!");
            LOGGER.error(e.toString());
        }
        BlessingFinderData.init();
        Keybinds.register();
        QuickWardrobe.register();
        QuickDungeon.register();
        PlayerStats.init();

        EssenceCounter essenceCounter = EssenceCounter.getInstance();
        HudRenderCallback.EVENT.register(essenceCounter::render);
        HudRenderCallback.EVENT.register(FPSRenderer::renderFPS);

        SheepRandomizer.registerSheepCommand();
        UpdateChecker updateChecker = new UpdateChecker();
        updateChecker.checkForUpdates();
        DungeonDodgeConnection dungeonDodgeConnection = new DungeonDodgeConnection();
        ClientPlayConnectionEvents.JOIN.register(dungeonDodgeConnection::onJoin);
        ClientPlayConnectionEvents.DISCONNECT.register(dungeonDodgeConnection::onDisconnect);

        LOGGER.info("DungeonDodge+ is ready!");
    }
}
