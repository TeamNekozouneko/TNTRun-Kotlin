package com.nekozouneko.tntrun.commands

import com.nekozouneko.tntrun.TNTRun
import com.nekozouneko.tntrun.utils.GameManager
import com.nekozouneko.tntrun.utils.Config
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.io.File


class ManageUI(
    private val plugin: TNTRun,
    private val config: FileConfiguration,
    private val configFile: File
): CommandExecutor, Listener {

    private val invTitle = "§cTNT§4Run §8管理画面"
    private val utils = Config(config, configFile)
    private val gm = GameManager(plugin, config)

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val player = sender as Player
        val inv = Bukkit.createInventory(player, 3*9, invTitle)

        if(plugin.gameState) {
            inv.setItem(10, getMeta(Material.RED_DYE, "§c停止する", listOf("§8ゲームを強勢終了させます。")))
        } else {
            inv.setItem(10, getMeta(Material.GREEN_DYE, "§a開始する", listOf("§8ゲームを開始します。")))
        }
        inv.setItem(12, getMeta(Material.ENDER_PEARL, "§bスポーン位置の設定", listOf("§8プレイヤーのスポーン位置を設定します。")))
        inv.setItem(13, getMeta(Material.ENDER_EYE, "§6ゲームスポーン位置の設定", listOf("§8ゲーム開始時、死亡時のスポーン位置を設定します。")))
        inv.setItem(14, getMeta(Material.TNT, "§cTNTパーティクルの表示", listOf("§8TNT破壊時にパーティクルを表示する。")))
        inv.setItem(16, getMeta(Material.BARRIER, "§c閉じる", listOf()))

        player.openInventory(inv)
        return true
    }

    private fun getMeta(material: Material, displayName: String, lore: List<String>): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta
        meta.setDisplayName(displayName)
        meta.lore = lore
        item.setItemMeta(meta)
        return item
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if(e.view.title == invTitle) {
            val player: HumanEntity = e.whoClicked
            when (e.slot) {
                10 -> {
                    if(plugin.gameState) {
                        gm.stopGame()
                    } else {
                        gm.startGame()
                    }
                    e.inventory.close()
                }
                12 -> utils.setSpawn(player)
                13 -> utils.setGameSpawn(player)
                14 -> utils.toggleTNTParticle(player)
                16 -> e.inventory.close()

            }
            e.isCancelled = true
        }
    }
}