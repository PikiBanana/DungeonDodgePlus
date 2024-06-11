package io.github.pikibanana.chat.handlers;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public interface ChatMessageHandler {
    boolean allowReceiveChatMessage(Text message, @Nullable SignedMessage signedMessage, @Nullable GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp);

    void onReceiveChatMessage(Text message, @Nullable SignedMessage signedMessage, @Nullable GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp);

    void onReceiveChatMessageCanceled(Text message, @Nullable SignedMessage signedMessage, @Nullable GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp);

    void sendCustomMessage(String message);

    void sendFormattedCustomMessage(Text message);

}
