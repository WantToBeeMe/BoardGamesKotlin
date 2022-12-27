package me.wtbm.boardgames.ttt

import me.wtbm.boardgames.BoardGames
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

import me.wtbm.boardgames.ttt.TTTGuiController.startGame
import me.wtbm.boardgames.ttt.TTTGuiController.playerInGame
import me.wtbm.boardgames.ttt.TTTGuiController.endGame
import me.wtbm.boardgames.ttt.TTTGuiController.refreshGUIForPlayer

object TTTGuiCommand : CommandExecutor {
    private val plugin get() = BoardGames.instance

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("only a player can run this command")
            return true
        }
        if (args.size < 1) {
            sender.sendMessage("specify what you want to do")
            return true
        }
        if (args[0] == "start") return start(sender, Arrays.copyOfRange(args, 1, args.size)) //Arrays.copyOfRange(args, 1, args.length) omdat ik geen zin heb om alle getallen aan te passen in de oude code
        else if (args[0] == "end") return end(sender)
        else if (args[0] == "open") return open(sender)
        else if (args[0] == "get") {
            sender.sendMessage( "${plugin.runningGames.size} game(s)" )
            return true;
        }
        return false
    }

    fun start(p: Player, args: Array<String>): Boolean {
        //still need to make a check if the players arent already in a game
        if (args.size == 1) {
            val player2: Player? = plugin.server.getPlayer(args[0])
            if (player2 == null) {
                p.sendMessage(args[0] + " §4is not online")
                return true
            }
            startGame(p, player2)
            return true

        }
        else if (args.size == 2) {
            val player1: Player? = plugin.server.getPlayer(args[0])
            val player2: Player? = plugin.server.getPlayer(args[1])
            if (player2 == null || player1 == null) {
                p.sendMessage(args[0] + " §4or§r " + args[1] + " §4is not online")
                return true
            }
            startGame(player1, player2)
            return true
        }
        return false
    }

    fun end(p: Player): Boolean {
        val check: Boolean = endGame(p)
        if (!check){
            p.sendMessage("§4you are not in a game")
            return true
        }
        p.sendMessage("§2stopped the game")

        return true
    }

    fun open(p: Player): Boolean{
        val id: UUID? = playerInGame(p)
        if(id == null){
            p.sendMessage("§4you are not in a game")
            return true;
        }
        refreshGUIForPlayer(id, p )
        //refreshGUI(id,0)
        return true;
    }
}