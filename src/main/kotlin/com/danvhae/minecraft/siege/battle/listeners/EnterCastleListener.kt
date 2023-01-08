package com.danvhae.minecraft.siege.battle.listeners

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
        castle.status = SiegeCastleStatus.UNDER_BATTLE
    }

}