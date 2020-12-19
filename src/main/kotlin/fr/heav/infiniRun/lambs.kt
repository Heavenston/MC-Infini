package fr.heav.infiniRun

import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.biomes.Biome
import net.minestom.server.world.biomes.BiomeEffects

val lambsBiome: Biome = Biome.builder()
        .category(Biome.Category.NONE)
        .precipitation(Biome.Precipitation.NONE)
        .downfall(0F)
        .effects(BiomeEffects.builder()
                .fogColor(0x000000)
                .foliageColor(0x555555)
                .grassColor(0x555555)
                .skyColor(0x000000)
                .waterColor(0xFF0000)
                .waterFogColor(0xFF3333)
                .build())
        .temperatureModifier(Biome.TemperatureModifier.NONE)
        .name(NamespaceID.from("heav:lambs"))
        .build()
