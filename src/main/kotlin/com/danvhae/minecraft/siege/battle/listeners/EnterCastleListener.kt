package com.danvhae.minecraft.siege.battle.listeners

import com.danvhae.minecraft.siege.battle.DVHSiegeBattle
import com.danvhae.minecraft.siege.core.DVHSiegeCore
import com.danvhae.minecraft.siege.core.enums.SiegeCastleStatus
import com.danvhae.minecraft.siege.core.events.EnterCastleEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener


class EnterCastleListener : Listener {

    @EventHandler
    fun onEnterCastle(event: EnterCastleEvent){
        val castle = event.castle
        castle.team?:return
        //Bukkit.getLogger().info("별 진입 ${castle.name} (${castle.ownerPlayer()?.team})")
        if(castle.status == SiegeCastleStatus.ELIMINATED){
            event.siegePlayer?.let {
                Bukkit.getPlayer(it.playerUUID)!!.let{player->
                    player.health = 0.0
                    player.sendMessage("별에 진입할 수 없습니다.")
                    //player.teleport(DVHSiegeCore.masterConfig.meetingRoom.toLocation()!!)
                    //player.sendMessage("별에 진입할 수 없습니다.")
                }

            }
            return
        }
        if(castle.status != SiegeCastleStatus.PEACEFUL)return

        event.siegePlayer?:return
        if(event.siegePlayer!!.team == castle.ownerPlayer()?.team)return
        if(!DVHSiegeCore.masterConfig.period){
            //event.player.health = 0.0 //공성시간 외 침입
            Bukkit.getScheduler().runTaskLater(DVHSiegeBattle.instance, {
                event.player.health = 0.0
            }, 2L)
            return
        }
        castle.status = SiegeCastleStatus.UNDER_BATTLE
    }

}