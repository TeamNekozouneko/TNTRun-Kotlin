package com.nekozouneko.tntrun.utils

import com.nekozouneko.tntrun.TNTRun
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class Timers(
    private val plugin: TNTRun
) {
    fun createTimer(
        initialTime: Int,
        minTime: Int,
        period: Long,
        onTick: (Int) -> Unit
    ): BukkitTask {
        return object : BukkitRunnable() {
            private var currentTime = initialTime

            override fun run() {
                if (currentTime >= minTime) {
                    onTick(currentTime)
                    currentTime--
                } else {
                    cancel()
                }
            }
        }.runTaskTimer(plugin, 0, period)
    }
}