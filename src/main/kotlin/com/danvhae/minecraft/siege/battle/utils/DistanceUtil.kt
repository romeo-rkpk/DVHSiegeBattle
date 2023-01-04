package com.danvhae.minecraft.siege.battle.utils

import com.danvhae.minecraft.siege.core.objects.SiegeCastle
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion
import org.bukkit.Location
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class DistanceUtil {
    companion object{
        fun distanceFromCastle(castle:SiegeCastle, location: Location):Double?{

            val region = WorldGuardPlugin.inst().getRegionManager(location.world).getRegion(castle.worldGuardID)
                    as? ProtectedCuboidRegion ?:return null

            val vecLoc = arrayOf(location.z, location.x)
            val vec:Array<Double> = arrayOf(0.0, 0.0)
            val vecMax = arrayOf(region.maximumPoint.z, region.maximumPoint.x)
            val vecMin = arrayOf(region.minimumPoint.z, region.minimumPoint.x)


            repeat(2){
                vec[it] = if(vecLoc[it] < vecMin[it])
                    abs(vecMin[it] - vecLoc[it])
                else if(vecLoc[it] in vecMin[it] .. vecMax[it])
                    0.0
                else if(vecMax[it] < vecLoc[it])
                    abs(vecMax[it] - vecLoc[it])
                else
                    return null
            }
            /*
            Bukkit.getLogger().info("========${id}===============")
            Bukkit.getLogger().info("${vec[0]}, ${vec[1]}")
            Bukkit.getLogger().info("vecMax : ${vecMax[0]} ${vecMax[1]}")
            Bukkit.getLogger().info("vecLoc: ${vecLoc[0]} ${vecLoc[0]}")
            Bukkit.getLogger().info("vecMin : ${vecMin[0]} ${vecMin[1]}")\
             */
            return sqrt(vec[0].pow(2.0) + vec[1].pow(2.0))
        }
    }
}