package com.danvhae.minecraft.siege.battle.listeners

import com.danvhae.minecraft.siege.battle.utils.ScoreBoardUtil
import com.danvhae.minecraft.siege.core.events.SiegeStartEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class SiegeStartListener : Listener {

    @EventHandler
    fun onSiegeStart(event:SiegeStartEvent){
        ScoreBoardUtil.nameVisible(true)
    }
}