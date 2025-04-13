package io.github.pikibanana;

import io.github.pikibanana.chat.ChatMessageHandlerImpl;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.dungeonapi.*;
import io.github.pikibanana.dungeonapi.essence.EssenceTracker;
import io.github.pikibanana.hud.HudRenderer;
import io.github.pikibanana.keybinds.Keybinds;
import io.github.pikibanana.keybinds.QuickDungeon;
import io.github.pikibanana.keybinds.QuickWardrobe;
import io.github.pikibanana.misc.SheepRandomizer;
import io.github.pikibanana.music.MusicManager;
import io.github.pikibanana.music.SoundRegistry;
import io.github.pikibanana.util.UpdateChecker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("DungeonDodge+");
    public static final String MOD_ID = "dungeondodgeplus";
    public static final String MOD_VERSION = "0.7-beta-recipe-pinning";
    public static DungeonDodgePlusConfig.Features features;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing DungeonDodge+...");

        // Load Config
        LOGGER.info("Loading configuration...");
        try {
            DungeonDodgePlusConfig.register();
            DungeonDodgePlusConfig.get();
            features = DungeonDodgePlusConfig.get().features;
            LOGGER.info("Configuration loaded successfully!");
        } catch (Exception e) {
            LOGGER.error("Failed to load configuration!", e);
        }

        // Register Event Handlers
        LOGGER.info("Registering event handlers...");
        try {
            ChatMessageHandlerImpl chatHandler = new ChatMessageHandlerImpl();
            ClientReceiveMessageEvents.ALLOW_CHAT.register(chatHandler::allowReceiveChatMessage);
            ClientReceiveMessageEvents.CHAT.register(chatHandler::onReceiveChatMessage);
            ClientReceiveMessageEvents.CHAT_CANCELED.register(chatHandler::onReceiveChatMessageCanceled);

            DungeonTracker dungeonTracker = new DungeonTracker();
            ClientReceiveMessageEvents.GAME.register(dungeonTracker::handleMessage);

            DungeonDodgeConnection connectionTracker = new DungeonDodgeConnection();
            ClientReceiveMessageEvents.ALLOW_GAME.register(connectionTracker::allowMessage);

            EssenceTracker essenceTracker = new EssenceTracker();
            ClientReceiveMessageEvents.GAME.register(essenceTracker::handleMessage);

            LOGGER.info("Event handlers registered successfully!");
        } catch (Exception e) {
            LOGGER.error("Failed to register event handlers!", e);
        }

        // Initialize Game Features
        LOGGER.info("Initializing game features...");
        BlessingFinderData.init();
        Keybinds.register();
        QuickWardrobe.register();
        QuickDungeon.register();
        PlayerStats.init();
        LOGGER.info("Game features initialized successfully!");

        // Register HUD Elements
        LOGGER.info("Registering HUD elements...");
        HudRenderer hudRenderer = new HudRenderer();
        HudRenderCallback.EVENT.register(hudRenderer::render);
        LOGGER.info("HUD elements registered!");

        // Register Music Features
        LOGGER.info("Registering music features...");
        SoundRegistry.registerAll();
        ClientTickEvents.END_CLIENT_TICK.register(MusicManager::tick);
        LOGGER.info("Music features registered!");

        // Register Additional Features
        LOGGER.info("Registering additional features...");
        SheepRandomizer.registerCommands();
        LOGGER.info("Additional features registered!");

        // Check for Updates
        LOGGER.info("Checking for updates...");
        UpdateChecker updateChecker = new UpdateChecker();
        updateChecker.checkForUpdates();

        // Register Connection Events
        LOGGER.info("Registering connection events...");
        DungeonDodgeConnection dungeonDodgeConnection = new DungeonDodgeConnection();
        ClientPlayConnectionEvents.JOIN.register(dungeonDodgeConnection::onJoin);
        ClientPlayConnectionEvents.DISCONNECT.register(dungeonDodgeConnection::onDisconnect);
        LOGGER.info("Connection events registered!");

        LOGGER.info("DungeonDodge+ has been successfully initialized!");
    }

}
