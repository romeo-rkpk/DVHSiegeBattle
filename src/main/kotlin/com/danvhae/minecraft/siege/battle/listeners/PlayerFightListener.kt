package com.danvhae.minecraft.siege.battle.listeners

import com.danvhae.minecraft.siege.battle.DVHSiegeBattle
import com.danvhae.minecraft.siege.battle.utils.DistanceUtil
import com.danvhae.minecraft.siege.core.DVHSiegeCore
import com.danvhae.minecraft.siege.core.objects.SiegeCastle
import com.danvhae.minecraft.siege.core.objects.SiegePlayer
import com.danvhae.minecraft.siege.core.objects.WorldConfiguration
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.math.max

class PlayerFightListener : Listener {

    companion object{
        private fun damageRatioCalculate(dist:Double):Double{
            return max(1.0 - dist / DVHSiegeBattle.NEAR_DISTANCE, 0.0)
        }
    }
    @EventHandler
    fun onFight(event:EntityDamageByEntityEvent){
        val victim = event.entity as? Player?:return
        val attacker = when (event.damager) {
            is Player -> event.damager as Player
            is Projectile -> {
                val projectile = event.damager as Projectile
                projectile.shooter as? Player ?:return
            }

            else -> return
        }

        //if(victim.location.world.name !in listOf("world", "star", "DIM1", DVHSiegeCore.masterConfig.wildWorldName))return
        //PvP 제어 월드에 없으면 판단 중지
        if(victim.location.world !in WorldConfiguration)return

        val siegeVictim = SiegePlayer.DATA[victim.uniqueId]?:return
        val siegeAttacker = SiegePlayer.DATA[attacker.uniqueId]?:return

        if(siegeVictim.team == siegeAttacker.team){
            event.isCancelled = true
            return
        }

        //if(victim.location.world.name == DVHSiegeCore.masterConfig.wildWorldName)return
        //별이 없는 월드이면 투사체 방호 혹은 데미지 경감에 대하여 계산할 이유 없음
        if(!WorldConfiguration[victim.location.world]!!.castleWorld)return
        Bukkit.getLogger().warning("데미지 경감 계산 중")
        var sum = 0.0
        var count = 0

        for(castle in SiegeCastle.DATA.values){
            //val distance = LocationUtil.
            val distance = DistanceUtil.distanceFromCastle(castle, victim.location)?:continue
            if(distance == 0.0)return
            val ratio = damageRatioCalculate(distance)
            if(ratio == 0.0)continue
            count++
            sum += ratio
        }

        if(count == 0){
            //Bukkit.getLogger().warning("별 근처에서 싸우는데 막상 가 보니 아닌 놈들이 있다고?")
            event.damage = 0.0
            return
        }
        event.damage *= (sum / count)

    }
}