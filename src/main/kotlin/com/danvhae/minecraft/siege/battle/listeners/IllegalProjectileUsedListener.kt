package com.danvhae.minecraft.siege.battle.listeners

import com.danvhae.minecraft.siege.battle.DVHSiegeBattle
import com.danvhae.minecraft.siege.battle.utils.DistanceUtil
import com.danvhae.minecraft.siege.core.objects.SiegeCastle
import com.danvhae.minecraft.siege.core.utils.math.R2Vector
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion
import org.bukkit.entity.Arrow
import org.bukkit.entity.ThrownPotion
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent

class IllegalProjectileUsedListener : Listener {

    @EventHandler
    fun onArrowFired(event:ProjectileLaunchEvent){
        val projectile = event.entity
        val k = when(projectile) {
            is Arrow, is ThrownPotion -> 0.01
            else -> return
        }

        val position = R2Vector(projectile.location.z, projectile.location.x)
        for(castle in SiegeCastle.DATA.values){
            if((DistanceUtil.distanceFromCastle(castle, projectile.location)?:continue) > DVHSiegeBattle.NEAR_DISTANCE)
                continue
            val region = WorldGuardPlugin.inst().getRegionManager(event.entity.location.world)
                .getRegion(castle.worldGuardID) as? ProtectedCuboidRegion ?:continue

            val minPoint = region.minimumPoint
            val maxPoint = region.maximumPoint

            val vectors = arrayOf(
                R2Vector(minPoint.z, minPoint.x) - position,
                R2Vector(minPoint.z, maxPoint.x) - position,
                R2Vector(maxPoint.z, minPoint.x) - position,
                R2Vector(maxPoint.z, maxPoint.x) - position
            )

        }
    }
}