package me.wtbm.boardgames.ttt

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player
import java.util.*

 object TTTChatController : TTTController {


    //initializing the X and O for tick tack toe
    private val VLine = TextComponent("§f|§r")
    private val HLine = TextComponent("§f--------§r")
    private  val used1 = TextComponent(" §4X§r ")
    private val used2 = TextComponent(" §1O§r ")
    private val gray = TextComponent(" §7#§r ")




     override fun doMove(p: Player, id: UUID, move: Int): Boolean {
         if(super.doMove(p, id, move)){
             clearChatForGame(id)
             printGame(id, isEndOfGame(id))
             return true
         }
         return false;
    }

     private fun clearChatForGame(id: UUID) {
        val game = plugin.runningGames[id]
        for (i in 0..99) {
            game!!.p1.sendMessage("\n")
            game.p2.sendMessage("\n")
        }
     }
     fun clearChatForGame(p: Player) {
         val id = playerInGame(p);
         if(id != null) {
             val game = plugin.runningGames[id]
             for (i in 0..99) {
                 game!!.p1.sendMessage("\n")
                 game.p2.sendMessage("\n")
             }
         }
     }


    override fun startGame(r1: Player, r2: Player):UUID {
        val id = super.startGame(r1, r2)
        val game = plugin.runningGames[id]!!

        game.p1.sendMessage("game: " + plugin.runningGames.size)
        game.p2.sendMessage("game: " + plugin.runningGames.size)
        game.p1.spigot().sendMessage(used1, game!!.p1t)
        game.p1.spigot().sendMessage(used2, game.p2t)
        game.p2.spigot().sendMessage(used1, game.p1t)
        game.p2.spigot().sendMessage(used2, game.p2t)
        printGame(id, 0)
        return id;
    }


    private fun getChar(fieldIndex: Int, placeSpot: Int, turn: Boolean, id: UUID): TextComponent? {
        if (fieldIndex == 0) {
            return if (!turn) gray else {
                val white2 = TextComponent(" §n§f#§r ")
                white2.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tttc move $id $placeSpot")
                white2
            }
        }
        return if (fieldIndex == 1) used1 else used2
    }

    private fun printGame(id: UUID, win: Int) {
        val game = plugin.runningGames[id]
        val field = game!!.field
        val tp = if (game.pl1turn) game.p1 else game.p2 //turning player
        val wp = if (game.pl1turn) game.p2 else game.p1 //waiting player
        val tpTurn = TextComponent("§f " + tp.name + "§7's turn§r")
        if (win == 0) wp.spigot().sendMessage(tpTurn)
        wp.spigot().sendMessage(getChar(field[0], 0, false, id), VLine, getChar(field[1], 1, false, id), VLine, getChar(field[2], 2, false, id))
        wp.spigot().sendMessage(HLine)
        wp.spigot().sendMessage(getChar(field[3], 3, false, id), VLine, getChar(field[4], 4, false, id), VLine, getChar(field[5], 5, false, id))
        wp.spigot().sendMessage(HLine)
        wp.spigot().sendMessage(getChar(field[6], 6, false, id), VLine, getChar(field[7], 7, false, id), VLine, getChar(field[8], 8, false, id))

        //ugly coding, i know
        if (win == 0) {
            tp.spigot().sendMessage(tpTurn)
            tp.spigot().sendMessage(getChar(field[0], 0, true, id), VLine, getChar(field[1], 1, true, id), VLine, getChar(field[2], 2, true, id))
            tp.spigot().sendMessage(HLine)
            tp.spigot().sendMessage(getChar(field[3], 3, true, id), VLine, getChar(field[4], 4, true, id), VLine, getChar(field[5], 5, true, id))
            tp.spigot().sendMessage(HLine)
            tp.spigot().sendMessage(
                getChar(field[6], 6, true, id), VLine, getChar(field[7], 7, true, id), VLine, getChar(field[8], 8, true, id))
        }
        else {
            val endMsg: TextComponent = if (win == 1) TextComponent("§f " + game.p1.name + " Won the game!!!")
                else if (win == 2) TextComponent("§f " + game.p2.name + "Won the game!!!")
                else TextComponent("§f its a tie :(")

            tp.spigot().sendMessage(getChar(field[0], 0, false, id), VLine, getChar(field[1], 1, false, id), VLine, getChar(field[2], 2, false, id))
            tp.spigot().sendMessage(HLine)
            tp.spigot().sendMessage(getChar(field[3], 3, false, id), VLine, getChar(field[4], 4, false, id), VLine, getChar(field[5], 5, false, id))
            tp.spigot().sendMessage(HLine)
            tp.spigot().sendMessage(getChar(field[6], 6, false, id), VLine, getChar(field[7], 7, false, id), VLine, getChar(field[8], 8, false, id))

            tp.spigot().sendMessage(endMsg)
            wp.spigot().sendMessage(endMsg)
            endGame(id)
        }
    }


}