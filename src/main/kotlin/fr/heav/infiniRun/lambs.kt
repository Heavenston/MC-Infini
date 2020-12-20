package fr.heav.infiniRun

import net.minestom.server.MinecraftServer
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.DimensionType
import net.minestom.server.world.biomes.Biome
import net.minestom.server.world.biomes.BiomeEffects
import java.util.*

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
        .name(NamespaceID.from("heav:lambs_biome"))
        .build()

val lambsDimensionType: List<DimensionType> = run {
        val dims = mutableListOf<DimensionType>()
        for (i in 0..5) {
                dims.add(DimensionType.builder(NamespaceID.from("heav:lambs_dim$i"))
                        .ultrawarm(false)
                        .natural(false)
                        .piglinSafe(false)
                        .respawnAnchorSafe(false)
                        .bedSafe(false)
                        .raidCapable(false)
                        .skylightEnabled(true)
                        .ceilingEnabled(false)
                        .fixedTime(Optional.of(18000))
                        .ambientLight(0.0f)
                        .logicalHeight(256)
                        .infiniburn(NamespaceID.from("minecraft:infiniburn_overworld"))
                        .build())
                MinecraftServer.getDimensionTypeManager().addDimension(dims[i])
        }
        dims
}