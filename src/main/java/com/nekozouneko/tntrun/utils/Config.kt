package com.nekozouneko.tntrun.utils

import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.HumanEntity
import java.io.File
import java.io.IOException

class Config(
    private val config: FileConfiguration,
    private val configFile: File
) {

    fun setSpawn(player: HumanEntity) {
        val loc = player.location
        config.set("spawn.x", loc.x)
        config.set("spawn.y", loc.y)
        config.set("spawn.z", loc.z)
        config.set("spawn.pitch", loc.pitch)
        player.sendMessage("§aスポーン位置を、「${loc.x}, ${loc.y}, ${loc.z}」 に設定しました。")
        config.save(configFile)
    }

    fun setGameSpawn(player: HumanEntity) {
        val loc = player.location
        config.set("gameSpawn.x", loc.x)
        config.set("gameSpawn.y", loc.y)
        config.set("gameSpawn.z", loc.z)
        config.set("gameSpawn.pitch", loc.pitch)
        player.sendMessage("§aゲームスポーン位置を、「${loc.x}, ${loc.y}, ${loc.z}」 に設定しました。")
        try {
            config.save(configFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun toggleTNTParticle(player: HumanEntity) {
        val isParticle = config.getBoolean("tnt_particle", false)
        config.set("tnt_particle", !isParticle)
        player.sendMessage("§8TNTパーティクルを、${if(!isParticle) "§a有効" else "§c無効"}§8にしました。")
    }

}