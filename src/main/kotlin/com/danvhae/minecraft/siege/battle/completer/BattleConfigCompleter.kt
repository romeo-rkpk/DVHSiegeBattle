package com.danvhae.minecraft.siege.battle.completer

import com.danvhae.minecraft.siege.battle.enums.ArrowDamageModel
import com.danvhae.minecraft.siege.core.utils.PermissionUtil
import com.danvhae.minecraft.siege.core.utils.TextUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class BattleConfigCompleter : TabCompleter {
    override fun onTabComplete(
        sender: CommandSender?, command: Command?, alias: String?, args: Array<out String>?
    ): MutableList<String> {
        if(sender == null || args == null)return arrayListOf()
        if(!PermissionUtil.supportTeamOrConsole(sender))return arrayListOf()
        val result = ArrayList<String>()
        if(args.size == 1){
            result.add("arrow-charge-model")
        }else if(args.size == 2){
            if(args[0] == "arrow-charge-model"){
                ArrowDamageModel.values().forEach { result.add(it.toString()) }
            }
        }
        return TextUtil.onlyStartsWith(result, args.last())
    }
}