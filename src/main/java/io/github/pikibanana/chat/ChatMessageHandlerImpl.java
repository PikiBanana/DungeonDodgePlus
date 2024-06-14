package io.github.pikibanana.chat;

import com.mojang.authlib.GameProfile;
import io.github.pikibanana.chat.handlers.ChatMessageHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public class ChatMessageHandlerImpl implements ChatMessageHandler {
    private final MinecraftClient client = MinecraftClient.getInstance();

    @Override
    public boolean allowReceiveChatMessage(Text message, @Nullable SignedMessage signedMessage, @Nullable GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp) {
        return true;
    }

    @Override
    public void onReceiveChatMessage(Text message, @Nullable SignedMessage signedMessage, @Nullable GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp) {
    }

    @Override
    public void onReceiveChatMessageCanceled(Text message, @Nullable SignedMessage signedMessage, @Nullable GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp) {
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

}
