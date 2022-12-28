package com.danvhae.minecraft.siege.battle.listeners

import com.danvhae.minecraft.siege.core.enums.SiegeCastleStatus
import com.danvhae.minecraft.siege.core.events.EnterCastleEvent
import com.danvhae.minecraft.siege.core.objects.SiegeCastle
import com.danvhae.minecraft.siege.core.objects.SiegePlayer
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

//import com.danvhae.minecraft.siege.


class EnterCastleListener : Listener {

    @EventHandler
    fun onEnterCastle(event: EnterCastleEvent){
        val castle = event.castle
        if(castle.status != SiegeCastleStatus.PEACEFUL)return
        if(castle.owner == null)return
        event.siegePlayer?:return
        if(event.siegePlayer!!.team == castle.ownerPlayer().team)return
        castle.status = SiegeCastleStatus.UNDER_BATTLE
    }

}