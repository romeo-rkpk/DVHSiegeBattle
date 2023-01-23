package com.danvhae.minecraft.siege.battle.listeners

import com.danvhae.minecraft.siege.core.DVHSiegeCore
import com.danvhae.minecraft.siege.core.enums.SiegeCastleStatus
import com.danvhae.minecraft.siege.core.events.SiegeEndEvent
import com.danvhae.minecraft.siege.core.objects.SiegePlayer
import com.danvhae.minecraft.siege.core.objects.WorldConfiguration
import com.danvhae.minecraft.siege.core.utils.FileUtil
import com.danvhae.minecraft.siege.core.utils.LocationUtil
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent
import java.util.*

class PlayerRespawnListener : Listener {

    companion object{
        private const val FILE_NAME = "respawnLocation-kotlin.dvh"
        private var savedLocation = HashMap<UUID, Location>()

        fun save(){
            val bytes = FileUtil.toBytes(savedLocation)!!
            FileUtil.writeBytes(bytes, FILE_NAME)
        }
    }

    init{
        val bytes = FileUtil.readBytes(FILE_NAME)
        savedLocation = bytes?.let { FileUtil.fromBytes(it) } as? HashMap<UUID, Location> ?: HashMap()
        save()
    }


    @EventHandler
    fun onPlayerDeath(event:PlayerDeathEvent){
        val location = event.entity.location
        if(location.world !in WorldConfiguration)return
        val siegePlayer = SiegePlayer.DATA[event.entity.uniqueId]?:return

        val castles = LocationUtil.locationAtStars(event.entity.location)
        val respawnLocation:Location = if(castles.isNotEmpty()) {
            val castle = castles.toTypedArray()[0]
            if(castle.ownerPlayer()?.team != siegePlayer.team)
                DVHSiegeCore.masterConfig.meetingRoom.toLocation()!!
            else if(castle.status == SiegeCastleStatus.UNDER_BATTLE)
                castle.workPosition
            else
                DVHSiegeCore.masterConfig.meetingRoom.toLocation()!!
        }else
            DVHSiegeCore.masterConfig.meetingRoom.toLocation()!!

        savedLocation[event.entity.uniqueId] = respawnLocation
        save()
    }

    @EventHandler
    fun onPlayerRespawn(event:PlayerRespawnEvent){
        val location = savedLocation[event.player.uniqueId]?:return
        savedLocation.remove(event.player.uniqueId)
        event.respawnLocation = location
        save()
    }

    @EventHandler
    fun onSiegeEnd(event:SiegeEndEvent){
        for(uuid in savedLocation.keys){
            savedLocation[uuid] = DVHSiegeCore.masterConfig.meetingRoom.toLocation()!!
        }
        save()
    }
}