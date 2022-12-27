package me.wtbm.boardgames.ttt

import me.wtbm.boardgames.BoardGames
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

interface TTTController {
    val plugin get() = BoardGames.instance

    fun generateGame(p1: Player, p2: Player): UUID {
        val id = UUID.randomUUID()
        val game = TTTGame(p1, p2)
        plugin.runningGames.put(id, game)
        Bukkit.getLogger().info("tic-tac-toe game started: $id")
        return id
    }

    fun doMove(p: Player, id: UUID, move: Int): Boolean {
        if (!plugin.runningGames.containsKey(id)) {
            Bukkit.getLogger().info("${p.name} tried the wrong uuid")
            return false
        }
        if (plugin.runningGames[id]?.isPlayersTurn(p) == false) {
            Bukkit.getLogger().info("${p.name} tried, but is the player")
            return false
        }
        if (plugin.runningGames[id]?.isPossibleMove(move) == false) {
            Bukkit.getLogger().info("${p.name} tried the wrong spot")
            return false
        }
        plugin.runningGames[id]!!.doMove(p, move)
        return true
    }

    fun isEndOfGame(id: UUID): Int { //0 = game still going,-1 = tie, 1 or 2 = win for player 1 or 2
        val a = plugin.runningGames[id]!!.isEndOfGame()
        return a
    }

    fun startGame(r1: Player, r2: Player): UUID {
        //assigning who is player 1 and 2 (will also depend on who goes first)
        val first = plugin.rand.nextInt(2) + 1 // 1 or 2
        val p1 = if (first == 1) r1 else r2
        val p2 = if (first == 1) r2 else r1

        return generateGame(p1, p2)
    }

    fun endGame(id: UUID): Boolean {
        try {
            plugin.runningGames.remove(id)
        } catch (e: Exception) {
            return false
        }
        return true
    }

    fun endGame(p: Player): Boolean {
        var bool = false;
        var id:  UUID? = null;
        plugin.runningGames.forEach{ (t, u) ->
            if(u.p1.uniqueId == p.uniqueId || u.p2.uniqueId == p.uniqueId){
                id = t;
                Bukkit.getLogger().info("afther ending1")
                return@forEach
            }
        }
        if(id == null) return false
        Bukkit.getLogger().info("afther ending2")
        return endGame(id!!)
    }

    fun playerInGame(p: Player) : UUID? {
        var id : UUID? = null;
        plugin.runningGames.forEach idk@{ t, u ->
            if (u.p1.uniqueId == p.uniqueId || u.p2.uniqueId == p.uniqueId) {
                id = t;
                return@idk;
            }
        }
        return id;

    }


}