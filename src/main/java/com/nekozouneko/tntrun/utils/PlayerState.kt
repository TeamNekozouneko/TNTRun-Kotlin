package com.nekozouneko.tntrun.utils

import com.nekozouneko.tntrun.TNTRun
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PlayerState(
    private val plugin: TNTRun
) {

    fun murderPlayer(player: Player) {
        plugin.alivePlayers.remove(player)
        plugin.deadPlayers.add(player)
        plugin.playerState[player] = false
    }

    fun playPlayer(player: Player) {
        plugin.alivePlayers.add(player)
        plugin.deadPlayers.remove(player)
        plugin.playPlayers.add(player)
        plugin.playerState[player] = true
    }


    fun resetAll() {
        plugin.playPlayers.clear()
        plugin.deadPlayers.clear()
        plugin.alivePlayers.clear()
    }

}