package io.github.pikibanana.dungeonapi.packet;

import io.github.pikibanana.Main;
import io.github.pikibanana.dungeonapi.item.ItemData;
import io.github.pikibanana.dungeonapi.packet.clientbound.DDItemS2CPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;

public class PacketListener {

    public void handleDungeonDodgeItemPayload(DDItemS2CPayload payload, ClientPlayNetworking.Context context) {
        Main.LOGGER.info("Received DungeonDodgeItem packet payload from server!");

        //String itemID = payload.itemID();
        ItemStack baseItem = payload.baseItem();

        ItemData.addItem("test", baseItem);
    }

}
