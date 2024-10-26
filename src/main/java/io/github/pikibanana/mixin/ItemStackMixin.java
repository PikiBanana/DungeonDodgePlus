package io.github.pikibanana.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.pikibanana.CustomModelDataFormats;
import io.github.pikibanana.Main;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.util.EnchantmentUtils;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Map;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {


    @Shadow
    @Final
    private ComponentMapImpl components;

    @Unique
    private static boolean isHasMatched(String enchantment) {
        String enchantmentName = enchantment.toLowerCase().trim();

        String[] enchantmentParts = enchantmentName.split(" ");
        String potentialNumber = enchantmentParts[enchantmentParts.length - 1].toUpperCase();
        int lastSpaceIndex = enchantmentName.lastIndexOf(' ');
        String potentialEnchantmentName = enchantmentName.substring(0, lastSpaceIndex != -1 ? lastSpaceIndex : enchantmentName.length() - 1)
                .replace(" ", "_").trim();

        boolean hasMatched = false;
        Map<String, String> maxLevelMap = EnchantmentUtils.getMaxLevelMap();

        if (maxLevelMap.containsKey(potentialEnchantmentName)) {
            String enchantmentLevel = maxLevelMap.get(potentialEnchantmentName);
            if (enchantmentLevel != null && enchantmentLevel.equals(potentialNumber)) {
                hasMatched = true;
            }
        }

        if (!hasMatched && maxLevelMap.containsKey("")) {
            String noLevelEnchant = maxLevelMap.get("");
            if (noLevelEnchant != null && !noLevelEnchant.isEmpty()) {
                hasMatched = enchantmentName.contains(noLevelEnchant);
            }
        }

        if (!hasMatched) {
            String enchantmentWithoutNumber = enchantmentName.replaceAll("[0-9]", "").trim().replace(" ", "_");

            if (maxLevelMap.containsKey(enchantmentWithoutNumber)) {
                hasMatched = true;
            }
        }

        return hasMatched;
    }

    @ModifyReturnValue(method = "getTooltip", at = @At("RETURN"))
    private List<Text> showCustomModelData(List<Text> original) {
        //custom model display on bottom of lore
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

    @ModifyReturnValue(method = "getTooltip", at = @At("RETURN"))
    private List<Text> colorMaxEnchantments(List<Text> original) {
        // Check if the colorMaxEnchantments feature is enabled
        if (DungeonDodgePlusConfig.get().features.colorMaxEnchantments.enabled) {
            // Set the formatting and color used for DungeonDodge enchantments
            Formatting dungeonDodgeEnchantmentFormatting = Formatting.BLUE;
            TextColor dungeonDodgeEnchantmentColor = TextColor.fromFormatting(dungeonDodgeEnchantmentFormatting);

            // Generate a rainbow gradient dynamically with 256 colors for a smooth animation.
            int[] rainbowGradient = EnchantmentUtils.generateRainbowGradient(256);
            // Smaller number in the division results in faster animation
            int rainbowIndex = (int) ((System.currentTimeMillis() / Main.features.colorMaxEnchantments.animationSpeed) % rainbowGradient.length);

            // Define the base color for non-rainbow max enchantments
            int baseColor = DungeonDodgePlusConfig.get().features.colorMaxEnchantments.enchantmentColor;

            // Iterate over the original lore lines
            for (int i = 0; i < original.size(); i++) {
                Text text = original.get(i);

                // Check if the current line matches the enchantment color style
                if (text.getStyle().getColor() != dungeonDodgeEnchantmentColor) continue;

                // Create a new mutable text to store the updated line
                MutableText newLine = Text.empty();
                String[] enchantments = text.getString().split(",");

                // Loop over each enchantment in the current line
                int elementNum = 1;
                for (String enchantment : enchantments) {
                    boolean hasMatched = isHasMatched(enchantment);


                    boolean noComma = ((i + 1 < original.size() && original.get(i + 1).getStyle().getColor() != dungeonDodgeEnchantmentColor)
                            && elementNum++ == enchantments.length);

                    // If it matches a max-level enchantment, apply the rainbow or base color
                    if (hasMatched) {
                        if (DungeonDodgePlusConfig.get().features.colorMaxEnchantments.isRainbow) {
                            MutableText rainbowText = Text.empty();

                            for (char c : enchantment.toCharArray()) {
                                rainbowIndex = (rainbowIndex + 1) % rainbowGradient.length;
                                int color = rainbowGradient[rainbowIndex];
                                rainbowText.append(Text.literal(c + "").setStyle(text.getStyle().withColor(TextColor.fromRgb(color))));
                            }

                            newLine.append(rainbowText);
                        } else {
                            newLine.append(Text.literal(enchantment).setStyle(text.getStyle().withColor(TextColor.fromRgb(baseColor))));
                        }
                        if (!noComma) newLine.append(Text.literal(",").formatted(dungeonDodgeEnchantmentFormatting));
                    } else {
                        newLine.append(Text.literal(enchantment + (noComma ? "" : ",")).formatted(dungeonDodgeEnchantmentFormatting));
                    }
                }

                // Replace the original line with the updated one
                original.set(i, newLine);
            }

        }
        return original;
    }


}
