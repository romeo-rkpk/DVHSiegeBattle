package com.danvhae.minecraft.siege.battle.listeners

import com.danvhae.minecraft.siege.core.enums.SiegeCastleStatus
import com.danvhae.minecraft.siege.core.events.EnterCastleEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

//import com.danvhae.minecraft.siege.


class EnterCastleListener : Listener {

    @EventHandler
    fun onEnterCastle(event: EnterCastleEvent){
        val castle = event.castle
        //Bukkit.getLogger().info("별 진입 ${castle.name} (${castle.ownerPlayer()?.team})")
        if(castle.status != SiegeCastleStatus.PEACEFUL)return
        if(castle.team == null)return
        event.siegePlayer?:return
        if(event.siegePlayer!!.team == castle.ownerPlayer()?.team)return
        castle.status = SiegeCastleStatus.UNDER_BATTLE
    }

}