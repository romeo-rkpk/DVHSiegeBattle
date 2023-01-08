package com.danvhae.minecraft.siege.battle.listeners

import com.danvhae.minecraft.siege.core.events.SiegeEndEvent
import com.danvhae.minecraft.siege.core.objects.SiegePlayer
import com.danvhae.minecraft.siege.core.utils.LocationUtil
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class SiegeEndListener : Listener {

    @EventHandler
    fun onSiegeEnd(event:SiegeEndEvent  ){
        //Bukkit.getLogger().warning("event called")
        for (player in Bukkit.getOnlinePlayers()) {
            val sPlayer = SiegePlayer[player.uniqueId]?:continue
            val castles = LocationUtil.locationAtStars(player.location)
            //Bukkit.getLogger().warning("${castles.toList()} (${player.name})")
            inner@for(c in castles){
                c.team?:continue
                if(c.team != sPlayer.team){
                    player.health = 0.0
                    break@inner
                }
            }
        }
    }
}