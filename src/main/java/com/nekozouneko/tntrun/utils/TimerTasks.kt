package com.nekozouneko.tntrun.utils

import com.nekozouneko.tntrun.TNTRun
import fr.mrmicky.fastboard.FastBoard
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.boss.BarColor
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player

class TimerTasks(
    private val plugin: TNTRun
) {

    fun runnable(player: Player) {

        val all = plugin.playPlayers.size
        val alive = plugin.alivePlayers.size
        val dead = plugin.deadPlayers.size
        val playerState = plugin.playerState[player]

        // Scoreboard
        val board = FastBoard(player)
        board.updateTitle("§cTNT§4Run")
        if(plugin.gameState) {
            board.updateLines(
                "",
                " §7状態§8: ${if(playerState == true) "§a生存" else "§c死亡"}",
                "",
                " §7プレイ者数§8: §a${all}人",
                " §7生存者数§8: §a${alive}人",
                " §7死亡数§8: §a${dead}人",
                "",
                " §enekozouneko.net  "
            )
        } else {
            board.updateLines(
                "",
                " §7ゲームが開始するまで、",
                " §7少々お待ちください。",
                "",
                " §enekozouneko.net"
            )
        }

        // Bossbar
        val bossBar = plugin.getBossBar()
        bossBar.color = BarColor.RED
        if (plugin.gameState) {
            bossBar.setTitle("§c生存者数§8: §a${plugin.alivePlayers.size}§8/§7${plugin.playPlayers.size}")
            val progress = alive / all.toDouble()
            bossBar.progress = progress.coerceAtMost(1.0)
        } else {
            bossBar.setTitle("§cTNT§4Run")
            bossBar.progress = 1.0
        }
        if (!bossBar.players.contains(player)) {
            bossBar.addPlayer(player)
        }
        bossBar.isVisible = true
    }
}