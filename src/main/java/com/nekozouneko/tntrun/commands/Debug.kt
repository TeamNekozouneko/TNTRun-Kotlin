package com.nekozouneko.tntrun.commands

import com.nekozouneko.tntrun.TNTRun
import com.nekozouneko.tntrun.utils.GameManager
import com.nekozouneko.tntrun.utils.PlayerState
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration

class Debug(
    private val plugin: TNTRun,
    private val config: FileConfiguration
): CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if(args?.get(0) == "stop") {
            sender.sendMessage("§8[§aデバッグ§8] §a強制停止しました。")
            GameManager(plugin, config).stopGame()
        } else if(args?.get(0) == "start") {
            sender.sendMessage("§8[§aデバッグ§8] §a開始しました。")
            GameManager(plugin, config).startGame()
        }
        return true
    }
}