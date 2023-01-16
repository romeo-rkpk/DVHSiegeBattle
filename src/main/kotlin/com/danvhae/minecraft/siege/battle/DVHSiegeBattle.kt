package com.danvhae.minecraft.siege.battle

import com.danvhae.minecraft.siege.battle.commands.BattleConfigCommand
import com.danvhae.minecraft.siege.battle.completer.BattleConfigCompleter
import com.danvhae.minecraft.siege.battle.listeners.*
import com.danvhae.minecraft.siege.battle.objects.BattleConfiguration
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class DVHSiegeBattle : JavaPlugin(){

    companion object{
        const val NEAR_DISTANCE = 100.0
        var instance: DVHSiegeBattle? = null
            get() {return field!!}
            private set
        internal var battleConfig = BattleConfiguration()
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
        pm.registerEvents(SiegeEndListener(), this)
        pm.registerEvents(ProjectileDamageListener(), this)

        getCommand("battle-config").let {
            it.executor = BattleConfigCommand()
            it.tabCompleter = BattleConfigCompleter()
        }
    }
}