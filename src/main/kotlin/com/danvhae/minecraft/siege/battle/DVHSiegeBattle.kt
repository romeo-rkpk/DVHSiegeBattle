package com.danvhae.minecraft.siege.battle

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class DVHSiegeBattle : JavaPlugin(){

    private var instance: DVHSiegeBattle? = null
        get() {return field!!}

    override fun onEnable() {
        instance = this
        Bukkit.getLogger().info("공성전 전투 제어 플러그인 활성화")
    }
}