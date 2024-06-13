package io.github.pikibanana.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.pikibanana.CustomModelDataFormats;
import io.github.pikibanana.DungeonDodgePlusConfig;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {


    @Shadow
    @Final
    private ComponentMapImpl components;

    @ModifyReturnValue(method = "getTooltip", at = @At("RETURN"))
    private List<Text> showCustomModelData(List<Text> original) {
        if (DungeonDodgePlusConfig.get().features.customModelDataDisplay.enabled) {
            ItemStack itemStack = (ItemStack) (Object) this;
            ComponentMap components = itemStack.getComponents();
            if (components.contains(DataComponentTypes.CUSTOM_MODEL_DATA)) {
                for (Component<?> component : components) {
                    if (component.type().equals(DataComponentTypes.CUSTOM_MODEL_DATA)) {
                        CustomModelDataComponent customModelDataComponent = (CustomModelDataComponent) component.value();
                        int customModelData = customModelDataComponent.value();

                        CustomModelDataFormats selectedFormat = DungeonDodgePlusConfig.get().features.customModelDataDisplay.format;
                        String formattedData = selectedFormat.format(customModelData);

                        original.add(
                                Text.literal("CustomModelData: ").formatted(Formatting.YELLOW)
                                        .append(Text.literal(formattedData).formatted(Formatting.AQUA))
                        );
                    }
                }
            }
        }
        return original;
    }

}
