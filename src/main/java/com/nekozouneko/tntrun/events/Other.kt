package com.nekozouneko.tntrun.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause

class Other: Listener {

    @EventHandler
    fun onPlayerDamage(e: EntityDamageEvent) {
        if(e.cause !== DamageCause.LAVA) {
            e.isCancelled = true
        }
    }

}