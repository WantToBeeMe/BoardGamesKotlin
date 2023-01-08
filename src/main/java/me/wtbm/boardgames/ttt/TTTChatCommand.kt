package me.wtbm.boardgames.ttt

import me.wtbm.boardgames.BoardGames
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

import me.wtbm.boardgames.ttt.TTTChatController.startGame
import me.wtbm.boardgames.ttt.TTTChatController.endGame
import me.wtbm.boardgames.ttt.TTTChatController.doMove
import me.wtbm.boardgames.ttt.TTTChatController.clearChatForGame

object TTTChatCommand : CommandExecutor{
    private val plugin get() = BoardGames.instance

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("only a player can run this command")
            return true
        }
        if (args.isEmpty()) {
            sender.sendMessage("specify what you want to do")
            return true
        }
        if (args[0] == "start") return start(sender, Arrays.copyOfRange(args, 1, args.size)) //Arrays.copyOfRange(args, 1, args.length) omdat ik geen zin heb om alle getallen aan te passen in de oude code
        else if (args[0] == "move") return move(sender, Arrays.copyOfRange(args, 1, args.size))
        else if (args[0] == "end") return end(sender)
        else if (args[0] == "get") {
            sender.sendMessage( "${plugin.runningGames.size} game(s)" )
            return true;
        }
        return false
    }

    private fun start(p: Player, args: Array<String>): Boolean {
        //still need to make a check if the players arent already in a game
        if (args.size == 1) {

            val player2: Player? = plugin.server.getPlayer(args[0])
            if (player2 == null) {
                p.sendMessage(args[0] + " §4is not online")
                return true
            }
            startGame(p, player2)
            return true
        } else if (args.size == 2) {
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
    private fun move(p: Player, args: Array<String>): Boolean {
        return try {
            val id = UUID.fromString(args[0])
            val move: Int = Integer.parseInt(args[1])
            val check: Boolean = doMove(p, id, move)
            if (!check) p.sendMessage("§4move not possible")
            true
        } catch (e: Exception) {
            p.sendMessage("§4invalid game id")
            true
        }
    }
    private fun end(p: Player): Boolean {
        clearChatForGame(p)
        val check: Boolean = endGame(p)
        if (!check) p.sendMessage("§4you are not in a game")
        else{
            p.sendMessage("§2stopped the game")
        }
        return true
    }


}