package io.github.pikibanana.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.pikibanana.CustomModelDataFormats;
import io.github.pikibanana.data.EnchantmentUtils;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
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
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {


    @Shadow
    @Final
    private ComponentMapImpl components;

    @ModifyReturnValue(method = "getTooltip", at = @At("RETURN"))
    private List<Text> showCustomModelData(List<Text> original) {
        //color the enchantments in the item lore
        if (DungeonDodgePlusConfig.get().features.colorMaxEnchantments.enabled) {
            //the enchantment color of the text to check. This should probably be fetched using the API at some point in case it changes
            Formatting dungeonDodgeEnchantmentFormatting = Formatting.BLUE;
            TextColor dungeonDodgeEnchantmentColor = TextColor.fromFormatting(dungeonDodgeEnchantmentFormatting);

            //the max enchantment color
            int baseColor = DungeonDodgePlusConfig.get().features.colorMaxEnchantments.enchantmentColor;
            int colorStartIndex = (int)((System.currentTimeMillis() / 50) % EnchantmentUtils.RAINBOW_GRADIENT.length);

            //check all lines in the original lore
            for (int i = 0; i < original.size(); i++) {
                //get the original line's value
                Text text = original.get(i);
                int rainbowIndex = colorStartIndex;

                //check to see if the original line is the color of the enchantment text
                if (text.getStyle().getColor() != dungeonDodgeEnchantmentColor) continue;
                //create the replacement line object
                MutableText newLine = Text.empty();

                //gets all enchantment texts on this current line
                String[] enchantments = text.getString().split(",");

                //loops over each enchantment text and applies the correct color code based on if it is maxed
                int elementNum = 1;
                for (String enchantment : enchantments) {
                    //gets the name of the enchantment from the text
                    String enchantmentName = enchantment.toLowerCase().replaceAll("/[0-9]/g", "");

                    //splits the text into sub-texts by spaces to try and see if the roman numeral at the end is a valid number to check
                    String[] potentialNumbers = enchantment.toUpperCase().split(" ");

                    //gets the text at the end (we can assume this is the roman numeral)
                    String potentialNumber = potentialNumbers[potentialNumbers.length - 1];

                    //checks if this roman numeral matches a max level value
                    boolean hasMatched = false;
                    if (EnchantmentUtils.MAX_LEVEL_MAP.containsKey(potentialNumber)) {
                        for (String match : EnchantmentUtils.MAX_LEVEL_MAP.get(potentialNumber)) {
                            if (enchantmentName.contains(match)) {
                                hasMatched = true;
                                break;
                            }
                        }
                    }

                    //checks if this element is the last in the line and the next line is empty
                    boolean noComma = ((i + 1 < original.size() && original.get(i + 1).getStyle().getColor() != dungeonDodgeEnchantmentColor)
                                && elementNum++ == enchantments.length);

                    //if the roman numeral matches a max level enchantment, format it accordingly
                    if (hasMatched) {
                        if (DungeonDodgePlusConfig.get().features.colorMaxEnchantments.isRainbow) {
                            MutableText rainbowText = Text.empty();

                            for (char c : enchantment.toCharArray()) {
                                rainbowIndex = (rainbowIndex + 1 < EnchantmentUtils.RAINBOW_GRADIENT.length ? rainbowIndex + 1 : 0);
                                int color = EnchantmentUtils.RAINBOW_GRADIENT[rainbowIndex];
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

                //update the lore with the new visuals
                original.set(i, newLine);
            }
        }
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

}
