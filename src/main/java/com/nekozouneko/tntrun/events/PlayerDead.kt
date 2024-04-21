package com.nekozouneko.tntrun.events

import com.nekozouneko.tntrun.TNTRun
import com.nekozouneko.tntrun.utils.GameManager
import com.nekozouneko.tntrun.utils.PlayerState
import com.nekozouneko.tntrun.utils.Timers
import org.apache.commons.lang.StringUtils
import org.bukkit.*
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.player.PlayerToggleSneakEvent

class PlayerDead(
    private val plugin: TNTRun,
    private val config: FileConfiguration
): Listener {

    @EventHandler
    fun onPlayerDamage(e: EntityDamageEvent) {
        if(e.entity is Player) {
            if(e.cause == DamageCause.LAVA) {
                val player = e.entity as Player
                if(plugin.gameState) {

                    val all = plugin.playPlayers.size
                    val alive = plugin.alivePlayers.size
                    val dead = plugin.deadPlayers.size
                    val playerState = plugin.playerState[player]

                    if(!plugin.deadPlayers.contains(player)) {
                        PlayerState(plugin).murderPlayer(player)
                        player.teleport(getLocation("gameSpawn"))
                        player.gameMode = GameMode.SPECTATOR
                        player.sendTitle("§c§l死んでしまった!", "§8あなたは§7${all}§8/§e${alive}位§8です。")
                        player.sendMessage("§4§l> §7あなたは、${all}位の中の、§e${alive}位§7になりました。")
                        Bukkit.broadcastMessage("§c死亡 §8> §c${player.name} §8[§7${alive}§8/§7${all}§8]")
                        if(plugin.alivePlayers.size == 1) {
                            plugin.gameState = false
                            val lastPlayer = plugin.alivePlayers[0]
                            wonPlayer(lastPlayer)
                        }
                    }
                } else {
                    player.teleport(getLocation("gameSpawn"))
                }
            }
        }
    }

    private fun wonPlayer(player: Player) {

        Bukkit.broadcastMessage(" §e§l§m========================================")
        Bukkit.broadcastMessage("")
        Bukkit.broadcastMessage(getCenterString("§6§lVICTORY"))
        Bukkit.broadcastMessage(getCenterString("§e§l${player.name}"))
        Bukkit.broadcastMessage("")
        Bukkit.broadcastMessage(getCenterString("§6§lこのプレイヤーが最後に生き残りました！"))
        Bukkit.broadcastMessage("")
        Bukkit.broadcastMessage(" §e§l§m========================================")
        for (loop in Bukkit.getOnlinePlayers()) {
            loop.teleport(player)
            loop.sendTitle("§e${player.name}", "§7さんが最後まで生き残りました！")
        }
        Timers(plugin).createTimer(3, 0, 40) {
            if(it !== 0) {
                for (loop in Bukkit.getOnlinePlayers()) {
                    launchFirework(loop)
                }
            } else {
                GameManager(plugin, config).stopGame()
            }
        }
    }

    private fun getCenterString(text: String): String {
        val chatWidth = 116
        val padding = (chatWidth - text.length) / 4
        return "${" ".repeat(padding)}$text${" ".repeat(padding)}"
    }

    private fun launchFirework(player: Player) {
        val fw = player.location.world.spawnEntity(player.location, EntityType.FIREWORK) as Firework
        val meta = fw.fireworkMeta
        val effect = FireworkEffect.builder()
            .withColor(Color.ORANGE)
            .with(FireworkEffect.Type.CREEPER)
            .withFade(Color.YELLOW)
            .withFlicker()
            .build()
        meta.addEffect(effect)
        meta.power = 0
        fw.fireworkMeta = meta
    }

    private fun getLocation(type: String): Location {
        val config = plugin.config
        val x = config.getDouble("${type}.x", 0.0)
        val y = config.getDouble("${type}.y", 0.0)
        val z = config.getDouble("${type}.z", 0.0)
        val pitch = config.getDouble("${type}.pitch", 0.0).toFloat()
        return Location(Bukkit.getWorld("world"), x, y, z, pitch, 0.0f)
    }

}