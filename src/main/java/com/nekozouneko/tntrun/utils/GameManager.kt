package com.nekozouneko.tntrun.utils

import com.nekozouneko.tntrun.TNTRun
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.scheduler.BukkitRunnable

class GameManager(
    private val plugin: TNTRun,
    private val config: FileConfiguration
) {

    private val state = PlayerState(plugin)

    fun startGame() {
        plugin.gameState = false
        state.resetAll()
        val x = config.getDouble("gameSpawn.x", 0.0)
        val y = config.getDouble("gameSpawn.y", 0.0)
        val z = config.getDouble("gameSpawn.z", 0.0)
        val pitch = config.getDouble("gameSpawn.pitch", 0.0).toFloat()
        val loc = Location(Bukkit.getWorld("world"), x, y, z, pitch, 0.0f)
        Bukkit.broadcastMessage("§a§l↓ §a今回の参加者リスト")
        for (player in Bukkit.getOnlinePlayers()) {
            Bukkit.broadcastMessage("§a◆ §2${player.name}")
            state.playPlayer(player)
            player.teleport(loc)
            player.gameMode = GameMode.ADVENTURE
        }
        Timers(plugin).createTimer(10, 0, 20) {
            Bukkit.broadcastMessage("§eゲーム開始まで、§e§l${it}§e秒です。")
            if(it == 10 || it == 5 || it == 3 || it == 2 || it == 1) {
                for (player in Bukkit.getOnlinePlayers()) {
                    player.sendTitle("§c§l${it}", "§7ゲーム開始まであと少しです...", 0, 20, 0)
                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                }
            }
            if(it == 0) {
                plugin.gameState = true
            }
        }
        for (player in Bukkit.getOnlinePlayers()) {
            player.sendTitle("§e§l開始", "§7ゲームが始まりました。")
            player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
        }
    }

    fun stopGame() {
        plugin.gameState = false
        val x = config.getDouble("spawn.x", 0.0)
        val y = config.getDouble("spawn.y", 0.0)
        val z = config.getDouble("spawn.z", 0.0)
        val pitch = config.getDouble("spawn.pitch", 0.0).toFloat()
        val loc = Location(Bukkit.getWorld("world"), x, y, z, pitch, 0.0f)
        for (player in Bukkit.getOnlinePlayers()) {
            state.resetAll()
            player.teleport(loc)
            player.gameMode = GameMode.ADVENTURE
        }
        var replacedCount = 0
        plugin.replaceBlocks.forEach {
            it.type = Material.TNT
            replacedCount++
        }
        plugin.replaceBlocks.clear()
        for (player in Bukkit.getOnlinePlayers()) {
            if(player.isOp) {
                player.sendMessage("§8[§aデバッグ§8] §a${replacedCount}ブロックの修復を行いました。")
            }
        }
        replacedCount = 0
    }

}