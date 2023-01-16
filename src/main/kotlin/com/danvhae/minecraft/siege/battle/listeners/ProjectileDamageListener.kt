package com.danvhae.minecraft.siege.battle.listeners

import com.danvhae.minecraft.siege.battle.DVHSiegeBattle
import com.danvhae.minecraft.siege.battle.enums.ArrowDamageModel
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class ProjectileDamageListener : Listener {

    companion object{
        const val MAX_PLAYER_ARROW_SPEED = 3.0
    }
    @EventHandler
    fun onProjectileHit(event:EntityDamageByEntityEvent){
        if(event.damager !is Arrow)return
        if(event.entity !is Player)return
        if((event.damager as? Arrow)?.shooter !is Player)return

        val velocity = event.damager.velocity
        when(val type = DVHSiegeBattle.battleConfig.arrow){
            ArrowDamageModel.NOT_CONTROL -> return
            ArrowDamageModel.KINETIC_ENERGY, ArrowDamageModel.KINETIC_ENERGY_LIMITED ->{
                val energy = velocity.x.pow(2) + velocity.y.pow(2) + velocity.z.pow(2)
                event.damage *= energy.let {
                    if(type == ArrowDamageModel.KINETIC_ENERGY_LIMITED)
                        min(1.0, energy / 9)
                    else
                        energy / 9
                }
            }

            ArrowDamageModel.MOMENTUM, ArrowDamageModel.MOMENTUM_LIMITED ->{
                val momentum = sqrt(velocity.x.pow(2) + velocity.y.pow(2) + velocity.z.pow(2))
                event.damage *= momentum.let{
                    if(type == ArrowDamageModel.MOMENTUM_LIMITED)
                        min(1.0, momentum / 3)
                    else
                        momentum / 3
                }
            }
        }

        /*
        val v = event.damager.velocity
        val velocitySquare = v.x.pow(2) + v.y.pow(2) + v.z.pow(2)

        event.damage *= velocitySquare / MAX_PLAYER_ARROW_SPEED.pow(2)

         */
    }
}