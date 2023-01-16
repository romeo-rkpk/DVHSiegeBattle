package com.danvhae.minecraft.siege.battle.commands

import com.danvhae.minecraft.siege.battle.DVHSiegeBattle
import com.danvhae.minecraft.siege.battle.enums.ArrowDamageModel
import com.danvhae.minecraft.siege.core.utils.PermissionUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class BattleConfigCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?
    ): Boolean {
        if(sender == null || args == null)return false
        if(!PermissionUtil.supportTeamOrConsole(sender))return false
        if(args.isEmpty()){
            listOf("/battle-config arrow-charge-model <MODEL>").forEach {
                sender.sendMessage(it)
            }
        }

        if(args[0] == "arrow-charge-model"){
            if(args.size != 1){

                try{
                    DVHSiegeBattle.battleConfig.arrow = ArrowDamageModel.valueOf(args[1])
                }catch (_:Exception){
                    sender.sendMessage("Invalid Model")
                    return false
                }
                sender.sendMessage("current model : ${DVHSiegeBattle.battleConfig.arrow}")
            }
        }
        return true
    }
}