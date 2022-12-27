package me.wtbm.boardgames

import me.wtbm.boardgames.ttt.TTTChatCommand
import me.wtbm.boardgames.ttt.TTTGame
import me.wtbm.boardgames.ttt.TTTGuiCommand
import me.wtbm.boardgames.ttt.TTTGuiListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.annotation.command.Command
import org.bukkit.plugin.java.annotation.command.Commands
import org.bukkit.plugin.java.annotation.dependency.Library
import org.bukkit.plugin.java.annotation.plugin.ApiVersion
import org.bukkit.plugin.java.annotation.plugin.Description
import org.bukkit.plugin.java.annotation.plugin.Plugin
import org.bukkit.plugin.java.annotation.plugin.author.Author
import java.util.*

@Plugin(name = "BoardGames", version ="1.0")
@ApiVersion(ApiVersion.Target.v1_19)
@Author("WantToBeeMe")
@Description("yea idk what i am doing lol")

@Commands(
    Command(name = "tttc", aliases = ["tic-tac-toe-chat" ], usage = "/tttc [start/move/end] [...]"),
    Command(name = "tttg", aliases = ["tic-tac-toe-gui"], usage = "/tttg [start/end/open] [...]")
)


@Library("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.22")
class BoardGames : JavaPlugin() {
    val rand = Random()
    var runningGames: MutableMap<UUID, TTTGame> = HashMap()

    companion object {
        lateinit var instance: BoardGames
    }

    override fun onEnable() {
        instance = this

        getCommand("tttc")?.setExecutor(TTTChatCommand)
        getCommand("tttg")?.setExecutor(TTTGuiCommand)
        TTTGuiListener(instance)

    }

    override fun onDisable() {
    }

}