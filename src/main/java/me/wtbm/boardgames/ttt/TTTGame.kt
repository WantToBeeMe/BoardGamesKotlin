package me.wtbm.boardgames.ttt

import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player

class TTTGame(val p1: Player, val p2: Player) {

    val p1t = TextComponent("§f" + p1.name)
    val p2t = TextComponent("§f" + p2.name)
    var pl1turn = true
    var field = intArrayOf(
        0, 0, 0,
        0, 0, 0,
        0, 0, 0
    )

    fun isEndOfGame(): Int {
        if (field[0] == field[1] && field[0] == field[2] && field[0] != 0) return field[0]      //A1 to A3
        else if (field[3] == field[4] && field[3] == field[5] && field[3] != 0) return field[3] //B1 to B3
        else if (field[6] == field[7] && field[6] == field[8] && field[6] != 0) return field[6] //C1 to C3

        else if (field[0] == field[3] && field[0] == field[6] && field[0] != 0) return field[0] //A1 to C1
        else if (field[1] == field[4] && field[1] == field[7] && field[1] != 0) return field[1] //A2 to C2
        else if (field[2] == field[5] && field[2] == field[8] && field[2] != 0) return field[2] //A3 to C3

        else if (field[0] == field[4] && field[0] == field[8] && field[0] != 0) return field[0] //A1 to C3
        else if (field[2] == field[4] && field[2] == field[6] && field[2] != 0) return field[2] //A3 to C1
        for (i in field) {
            if (i == 0) return 0
        }
        return -1
    }

    fun doMove(p: Player, move: Int) {
        val pint: Int
        pint =
            if (pl1turn)
                if (p.uniqueId == p1.uniqueId) 1
                else 2
            else
                if (p.uniqueId == p2.uniqueId) 2
                else 1
        field[move] = pint
        pl1turn = !pl1turn
    }

    public fun isPlayersTurn(p: Player): Boolean {
        return if (pl1turn) (p.uniqueId == p1.uniqueId) else (p.uniqueId == p2.uniqueId)
    }

    fun isPossibleMove(move: Int): Boolean {
        return if (move < 0 || move >= field.size) false else field[move] == 0
    }
}