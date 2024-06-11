package io.github.pikibanana.chat;

import com.mojang.authlib.GameProfile;
import io.github.pikibanana.DungeonDodgePlusConfig;
import io.github.pikibanana.chat.handlers.ChatMessageHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ChatMessageHandlerImpl implements ChatMessageHandler {
    private final MinecraftClient client = MinecraftClient.getInstance();

    @Override
    public boolean allowReceiveChatMessage(Text message, @Nullable SignedMessage signedMessage, @Nullable GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp) {
        return false;
    }

    @Override
    public void onReceiveChatMessage(Text message, @Nullable SignedMessage signedMessage, @Nullable GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp) {
    }


    @Override
    public void onReceiveChatMessageCanceled(Text message, @Nullable SignedMessage signedMessage, @Nullable GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp) {
        if (client.player != null) {
            ChatHud chatHud = client.inGameHud.getChatHud();
            if (DungeonDodgePlusConfig.get().features.timestamp.enabled
            ) {
                chatHud.addMessage(addTimestamp(Text.literal(message.toString())));
            } else {
                chatHud.addMessage(Text.literal(message.toString()));
            }
        }
    }

    @Override
    public void sendCustomMessage(String message) {
        if (client.player != null) {
            client.player.sendMessage(Text.literal(message), false);
        }
    }

    @Override
    public void sendFormattedCustomMessage(Text message) {
        if (client.player != null) {
            client.player.sendMessage(message, false);
        }
    }

    public Text addTimestamp(Text text) {
        ZoneId userTimeZone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.now(), userTimeZone);
        String timestamp = String.format("[%02d:%02d:%02d] ", localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond());


        return Text.of(timestamp).copy().append(text);
    }

    public Text modifyChatMessage(Text text, boolean b) {
        if (DungeonDodgePlusConfig.get().features.timestamp.enabled) {
            ZoneId userTimeZone = ZoneId.systemDefault();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.now(), userTimeZone);
            String timestamp = String.format("[%02d:%02d:%02d] ", localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond());

            return Text.of(timestamp).copy().append(text);
        } else {
            return text;
        }
    }

}
