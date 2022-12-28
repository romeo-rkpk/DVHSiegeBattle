package com.danvhae.minecraft.siege.battle

import com.danvhae.minecraft.siege.battle.listeners.EnterCastleListener
import com.danvhae.minecraft.siege.battle.listeners.LeaveCastleListener
import com.danvhae.minecraft.siege.battle.listeners.PlayerFightListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class DVHSiegeBattle : JavaPlugin(){

    private var instance: DVHSiegeBattle? = null
        get() {return field!!}

    override fun onEnable() {
        instance = this
        Bukkit.getLogger().info("공성전 전투 제어 플러그인 활성화")

        val pm = Bukkit.getPluginManager()

        pm.registerEvents(EnterCastleListener(), this)
        pm.registerEvents(LeaveCastleListener(), this)
        pm.registerEvents(PlayerFightListener(), this)
    }
}