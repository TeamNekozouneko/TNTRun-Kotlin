package com.nekozouneko.tntrun.events

import com.nekozouneko.tntrun.TNTRun
import com.nekozouneko.tntrun.utils.PlayerState
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerJoin(
    private val config: FileConfiguration,
    private val plugin: TNTRun
) : Listener {

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        if(!plugin.playPlayers.contains(e.player)) {
            val x = config.getDouble("spawn.x", 0.0)
            val y = config.getDouble("spawn.y", 0.0)
            val z = config.getDouble("spawn.z", 0.0)
            val pitch = config.getDouble("spawn.pitch", 0.0).toFloat()
            val loc = Location(Bukkit.getWorld("world"), x, y, z, pitch, 0.0f)
            e.player.teleport(loc)
            if(plugin.gameState) {
                if(plugin.playPlayers.contains(e.player)) {
                    PlayerState(plugin).murderPlayer(e.player)
                    e.player.gameMode = GameMode.SPECTATOR
                    e.player.sendMessage("§cあなたはゲーム中に退席したため、死亡状態となりました。")
                } else {
                    e.player.sendMessage("§cあなたはすでにゲームが開始している中での参加なため、観戦状態となりました。次のゲームまでお待ちください。")
                    e.player.gameMode = GameMode.SPECTATOR
                }
            } else {
                e.player.gameMode = GameMode.ADVENTURE
            }
        }
        plugin.getBossBar().addPlayer(e.player)
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        if(plugin.playPlayers.contains(e.player)) {
            if(plugin.alivePlayers.contains(e.player)) {
                PlayerState(plugin).murderPlayer(e.player)
            }
        }
        plugin.getBossBar().removePlayer(e.player)
    }
}