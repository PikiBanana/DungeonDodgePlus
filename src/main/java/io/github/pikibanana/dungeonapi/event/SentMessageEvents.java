package io.github.pikibanana.dungeonapi.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Client message event modification and cancellation implementation.
 * @author BasicallyIAmFox
 */
@Environment(EnvType.CLIENT)
public final class SentMessageEvents {
    public enum ReturnState {
        DEFAULT,
        CANCEL;
    }

    @FunctionalInterface
    public interface OnSentMessage {
        ReturnState onSentMessage(MinecraftClient client, String message);
    }

    public static final Event<OnSentMessage> CHAT_ON = EventFactory.createArrayBacked(OnSentMessage.class, (callbacks) -> (client, message) -> {
        for (OnSentMessage callback : callbacks) {
            ReturnState value = callback.onSentMessage(client, message);
            if (value != ReturnState.DEFAULT)
                return value;
        }

        return ReturnState.DEFAULT;
    });

    public static final Event<OnSentMessage> OVERLAY_ON = EventFactory.createArrayBacked(OnSentMessage.class, (callbacks) -> (client, message) -> {
        for (OnSentMessage callback : callbacks) {
            ReturnState value = callback.onSentMessage(client, message);
            if (value != ReturnState.DEFAULT)
                return value;
        }

        return ReturnState.DEFAULT;
    });

    @FunctionalInterface
    public interface ModifySentMessage {
        @Nullable Text modifySentMessage(MinecraftClient client, @NotNull Text message);
    }

    public static final Event<ModifySentMessage> CHAT_MODIFY = EventFactory.createArrayBacked(ModifySentMessage.class, (callbacks) -> (client, message) -> {
        for (ModifySentMessage callback : callbacks) {
            Text value = callback.modifySentMessage(client, message);
            if (value != null)
                return value;
        }

        return null;
    });
}
