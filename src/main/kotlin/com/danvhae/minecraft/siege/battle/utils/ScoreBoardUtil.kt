package com.danvhae.minecraft.siege.battle.utils

import org.bukkit.Bukkit
import org.bukkit.scoreboard.Team

class ScoreBoardUtil {

    companion object{
        fun nameVisible(show:Boolean){
            val scoreboard = Bukkit.getScoreboardManager().mainScoreboard
            scoreboard.teams.forEach{team ->
                team.setOption(Team.Option.NAME_TAG_VISIBILITY,
                    if(show) Team.OptionStatus.ALWAYS else Team.OptionStatus.FOR_OWN_TEAM)
            }
        }
    }
}