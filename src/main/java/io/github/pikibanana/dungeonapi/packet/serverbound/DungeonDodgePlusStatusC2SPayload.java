package io.github.pikibanana.dungeonapi.packet.serverbound;

import io.github.pikibanana.Main;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record DungeonDodgePlusStatusC2SPayload(String version) implements CustomPayload {
    public static final Identifier DUNGEONDODGE_PLUS_STATUS_PAYLOAD_ID = Identifier.of(Main.MOD_ID, "status");
    public static final CustomPayload.Id<DungeonDodgePlusStatusC2SPayload> ID = new Id<>(DUNGEONDODGE_PLUS_STATUS_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, DungeonDodgePlusStatusC2SPayload> CODEC =
            PacketCodec.tuple(PacketCodecs.STRING, DungeonDodgePlusStatusC2SPayload::version, DungeonDodgePlusStatusC2SPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
