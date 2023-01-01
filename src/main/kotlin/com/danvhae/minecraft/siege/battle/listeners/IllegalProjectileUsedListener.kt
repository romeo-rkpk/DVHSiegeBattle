package com.danvhae.minecraft.siege.battle.listeners

import com.danvhae.minecraft.siege.battle.DVHSiegeBattle
import com.danvhae.minecraft.siege.battle.utils.DistanceUtil
import com.danvhae.minecraft.siege.core.objects.SiegeCastle
import com.danvhae.minecraft.siege.core.utils.math.Interval
import com.danvhae.minecraft.siege.core.utils.math.R2Matrix
import com.danvhae.minecraft.siege.core.utils.math.R2Vector
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion
import org.bukkit.Bukkit
import org.bukkit.entity.Arrow
import org.bukkit.entity.Projectile
import org.bukkit.entity.ThrownPotion
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.ceil
import kotlin.math.ln

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

            val unitVector = Array( 4) { vectors[it] / vectors[it].length() }

            var u1:R2Vector? = null
            var u2:R2Vector? = null
            var minDotValue = 2.0
            outer@for(i in 0..3)
                inner@for(j in 0..3){
                    if(i >= j)continue@inner

                    val dotValue = unitVector[i] * unitVector[j]
                    if(dotValue < minDotValue){
                        u1 = unitVector[i]
                        u2 = unitVector[j]
                        minDotValue = dotValue
                    }
                }

            u1?:continue
            u2?:continue

            val v = R2Vector(projectile.velocity.z, projectile.velocity.x)
            val w = (R2Matrix(u1, u2).inverse() ?:continue) * v

            if(DistanceUtil.distanceFromCastle(castle, projectile.location)!! > 0.0){
                if(w[0] < 0 || w[1] < 0) continue
                if(!(w[0] > 0 || w[1] > 0)) continue
            }
            //자, 이제 벡터 v는 확실히 별을 향하고 있다

            //이제 벡터 v의 각 성분이 영역 R 내부에 있는 시간 t의 범위를 구"해야"한다


            val distanceZX = arrayOf(Interval(minPoint.z, maxPoint.z), Interval(minPoint.x, maxPoint.x))
            //벡터의 각 성분에 대하여 이하를 반복한다.
            val timeZX = ArrayList<Interval>()
            repeat(2){
                //val start = if(position[it] in minPoint.z .. maxPoint.)

                val highDot = v[it] * (distanceZX[it].high - position[it])
                val lowDot = v[it] * (distanceZX[it].low - position[it])

                val end = time(
                    if(highDot > lowDot){distanceZX[it].high}else{distanceZX[it].low} - position[it],
                    k,
                    v[it]
                )
                val start = time(
                    if(position[it] in distanceZX[it]){
                        0.0
                    }else{
                        val result = if(lowDot < highDot){
                            distanceZX[it].low
                        }else{
                            distanceZX[it].high
                        } - position[it]
                        result
                    },
                    k,
                    v[it]
                )

                timeZX.add(Interval(start, end))

            }
            val timeInterval = timeZX[0].intersect(timeZX[1])?:continue
            val removeProjectileTime = if(timeZX[0].low == 0.0 && timeZX[1].low == 0.0){
                if(timeInterval.high == Double.POSITIVE_INFINITY) null
                else timeInterval.high
            }else{
                timeInterval.low
            } ?: continue

            PROJECTILE_TABLE[projectile.uniqueId] = projectile
            TASK_ID[projectile.uniqueId] = Bukkit.getScheduler().runTaskLater(
                DVHSiegeBattle.instance,
                {
                    val p = PROJECTILE_TABLE[projectile.uniqueId]
                    p?.remove()
                    PROJECTILE_TABLE.remove(p?.uniqueId)
                    TASK_ID.remove(p?.uniqueId)
                },
                ceil(removeProjectileTime).toLong()
            ).taskId
            break
        }
    }

    @EventHandler
    fun onProjectileHit(event:ProjectileHitEvent){
        val uuid = event.entity.uniqueId
        val taskID = TASK_ID[uuid]?:return
        Bukkit.getScheduler().cancelTask(taskID)
        //PROJECTILE_TABLE[uuid]?.remove()
        PROJECTILE_TABLE.remove(uuid)
    }


    companion object{
        private val PROJECTILE_TABLE = HashMap<UUID, Projectile>()
        private val TASK_ID = HashMap<UUID, Int>()
        private fun time(distance: Double, drag: Double, initialVelocity: Double): Double {
            //Bukkit.getLogger().info("d:${distance}, k:${drag} v0:${initialVelocity}")
            val innerLn = 1 - drag * distance / initialVelocity
            if (innerLn <= 0.0) return Double.POSITIVE_INFINITY
            return -ln(innerLn) / drag
        }
    }
}