package com.nekozouneko.tntrun.events

import com.nekozouneko.tntrun.TNTRun
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.math.Vector3
import com.sk89q.worldedit.world.World
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.ApplicableRegionSet
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerMoveEvent

class TntBreak(
    private val plugin: TNTRun,
    private val config: FileConfiguration
): Listener {

    private val breakDelay = 5L

    @EventHandler
    fun onSnowHit(e: ProjectileHitEvent) {
        if (e.entity.shooter is Player) {
            val player = e.entity.shooter as Player
            val loc = e.hitBlock!!.location
            val block = loc.block
            if (e.entity.type == EntityType.SNOWBALL && block.type == Material.TNT) {
                e.entity.world.createExplosion(loc, 5.0f, false, false)
                launchFirework(player, block.location)
                player.sendMessage("§c地面に当たりました！")
            }
        }
    }

    private fun launchFirework(player: Player, location: Location) {
        val fw = location.world.spawnEntity(location, EntityType.FIREWORK) as Firework
        val meta = fw.fireworkMeta
        val effect = FireworkEffect.builder()
            .withColor(Color.RED)
            .with(FireworkEffect.Type.BALL)
            .withFade(Color.ORANGE)
            .withFlicker()
            .build()
        meta.power = 0
        meta.addEffect(effect)
        fw.fireworkMeta = meta
    }

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        if (plugin.gameState) {
            val player = e.player
            val loc = player.location
            val below = loc.subtract(0.0, 1.0, 0.0).block
            decrementBelow(below)
        }
    }

    private fun decrementBelow(block: Block) {
        if (block.type == Material.TNT && inArena(block)) {
            plugin.server.scheduler.runTaskLater(plugin, Runnable {
                block.type = Material.AIR
                val isParticle = config.getBoolean("tnt_particle", false)
                if(isParticle) {
                    block.location.world.spawnParticle(Particle.BLOCK_CRACK, block.location, 10, Material.TNT.createBlockData())
                }
                plugin.replaceBlocks.add(block)
            }, breakDelay)
        }
    }

    private fun inArena(block: Block): Boolean {
        val worldGuard = WorldGuard.getInstance()
        val bukkitWorld = block.world
        val worldEditWorld = WorldGuard.getInstance().platform.matcher.getWorldByName(bukkitWorld.name)
        val arena = worldGuard.platform.regionContainer.get(worldEditWorld)?.getRegion("arena")
        val loc = block.location
        val location = BlockVector3.at(loc.x, loc.y, loc.z)
        return arena?.contains(location) ?: false
    }
}