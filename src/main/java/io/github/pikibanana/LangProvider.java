package io.github.pikibanana;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class LangProvider extends FabricLanguageProvider {
    public LangProvider(FabricDataOutput dataGenerator, CompletableFuture<RegistryWrapper.WrapperLookup> registryFuture) {
        super(dataGenerator, registryFuture);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        String configPrefix = "text.autoconfig.DungeonDodgePlus.option.features";
        translationBuilder.add(configPrefix + ".essenceFinder", "Essence Finder");
        translationBuilder.add(configPrefix + ".essenceFinder.enabled", "Essence Finder Enabled");
        translationBuilder.add(configPrefix + ".essenceFinder.color", "Essence Finder Color");
    }
}
