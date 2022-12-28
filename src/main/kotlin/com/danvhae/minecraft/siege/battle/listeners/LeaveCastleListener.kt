package com.danvhae.minecraft.siege.battle.listeners

import com.danvhae.minecraft.siege.core.enums.SiegeCastleStatus
import com.danvhae.minecraft.siege.core.events.LeaveCastleEvent
import com.danvhae.minecraft.siege.core.objects.SiegeCastle
import com.danvhae.minecraft.siege.core.objects.SiegePlayer
import com.danvhae.minecraft.siege.core.utils.PlayerUtil
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class LeaveCastleListener : Listener {

    @EventHandler
    fun onLeaveCastle(event:LeaveCastleEvent){
        //Bukkit.getLogger().info("leaving ${event.castle.name}")
        val castle = event.castle
        if(castle.status != SiegeCastleStatus.UNDER_BATTLE)return
        val ownerTeam = castle.ownerPlayer().team
        val leaverTeam = event.siegePlayer?.team?:return
        if(ownerTeam == leaverTeam)return
        //Bukkit.getLogger().info("line 21")
        for(player in Bukkit.getOnlinePlayers()){
            if(player.uniqueId == event.player.uniqueId)continue
            val region = PlayerUtil.playerRegion(player)
            val siege = SiegePlayer.DATA[player.uniqueId]?:continue
            if(siege.team == ownerTeam)continue

            if(castle.worldGuardID in region)return
        }

        castle.status = SiegeCastleStatus.PEACEFUL
        Bukkit.getLogger().info("PEACE ${castle.name} : ${SiegeCastle.DATA[castle.id]!!.status}")

    }
}