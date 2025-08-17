package io.github.pikibanana.dungeonapi.packet.clientbound;

import io.github.pikibanana.Main;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record DDItemS2CPayload(ItemStack baseItem) implements CustomPayload {
    public static final Id<DDItemS2CPayload> ID = new Id<>(Identifier.of(Main.MOD_ID, "send_dungeondodge_item"));
    public static final PacketCodec<RegistryByteBuf, DDItemS2CPayload> CODEC =
            PacketCodec.tuple(ItemStack.PACKET_CODEC, DDItemS2CPayload::baseItem, DDItemS2CPayload::new);
    //PacketCodecs.STRING, DDItemS2CPayload::itemID, on above line

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
