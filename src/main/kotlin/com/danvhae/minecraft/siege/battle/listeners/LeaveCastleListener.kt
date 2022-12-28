package com.danvhae.minecraft.siege.battle.listeners

import com.danvhae.minecraft.siege.core.enums.SiegeCastleStatus
import com.danvhae.minecraft.siege.core.events.LeaveCastleEvent
import com.danvhae.minecraft.siege.core.objects.SiegePlayer
import com.danvhae.minecraft.siege.core.utils.PlayerUtil
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class LeaveCastleListener : Listener {

    @EventHandler
    fun onLeaveCastle(event:LeaveCastleEvent){
        val castle = event.castle
        if(castle.status != SiegeCastleStatus.UNDER_BATTLE)return
        for(player in Bukkit.getOnlinePlayers()){
            if(player.uniqueId == event.player.uniqueId)continue
            val region = PlayerUtil.playerRegion(player)
            val siege = SiegePlayer.DATA[player.uniqueId]?:continue
            if(castle.ownerPlayer().team == siege.team)continue
            if(castle.worldGuardID in region)return
        }

        castle.status = SiegeCastleStatus.PEACEFUL
    }
}