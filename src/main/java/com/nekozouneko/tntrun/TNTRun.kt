package com.nekozouneko.tntrun

import com.nekozouneko.tntrun.commands.Debug
import com.nekozouneko.tntrun.commands.ManageUI
import com.nekozouneko.tntrun.events.Other
import com.nekozouneko.tntrun.events.PlayerDead
import com.nekozouneko.tntrun.events.PlayerJoin
import com.nekozouneko.tntrun.events.TntBreak
import com.nekozouneko.tntrun.utils.TimerTasks
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class TNTRun : JavaPlugin() {

    var gameState = false

    private lateinit var configFile: File
    private lateinit var config: FileConfiguration
    private lateinit var bossBar: BossBar

    var playPlayers = mutableListOf<Player>()
    val alivePlayers = mutableListOf<Player>()
    val deadPlayers = mutableListOf<Player>()
    val playerState = mutableMapOf<Player, Boolean>()

    val replaceBlocks = mutableListOf<Block>()

    override fun onEnable() {

        configFile = File(dataFolder, "config.yml")
        configFile.parentFile.mkdirs()
        if(!configFile.exists()) {
            configFile.createNewFile()
            logger.info("新しくコンフィグファイルを生成しました。")
        }
        config = YamlConfiguration.loadConfiguration(configFile)

        val listeners = listOf(
            TntBreak(this, config),
            ManageUI(this, config, configFile),
            PlayerJoin(config, this),
            PlayerDead(this, config),
            Other()
        )
        val commands = listOf(
            "debug" to Debug(this, config),
            "tntrun" to ManageUI(this, config, configFile)
        )

        listeners.forEach {
            server.pluginManager.registerEvents(it, this)
        }
        commands.forEach { (name, cmd) ->
            getCommand(name)?.setExecutor(cmd)
        }

        server.scheduler.runTaskTimer(this, Runnable {
            for (player in server.onlinePlayers) {
                TimerTasks(this).runnable(player)
            }
        }, 5L, 5L)

        bossBar = Bukkit.createBossBar("§cTNT§4Run", BarColor.RED, BarStyle.SOLID)
        bossBar.progress = 1.0
        bossBar.color = BarColor.RED

        logger.info("TNTRunがこんにちはしました。")
    }

    override fun onDisable() {
        logger.info("TNTRunがばいばいしました。")
    }

    fun getBossBar(): BossBar {
        return bossBar
    }
}
