package com.danvhae.minecraft.siege.battle

import com.danvhae.minecraft.siege.battle.listeners.*
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class DVHSiegeBattle : JavaPlugin(){

    companion object{
        const val NEAR_DISTANCE = 100.0
        var instance: DVHSiegeBattle? = null
            get() {return field!!}
            private set
    }


    override fun onEnable() {
        instance = this
        Bukkit.getLogger().info("공성전 전투 제어 플러그인 활성화")

        val pm = Bukkit.getPluginManager()

        pm.registerEvents(EnterCastleListener(), this)
        pm.registerEvents(LeaveCastleListener(), this)
        pm.registerEvents(PlayerFightListener(), this)
        pm.registerEvents(IllegalProjectileUsedListener(), this)
        pm.registerEvents(PlayerRespawnListener(), this)
    }
}