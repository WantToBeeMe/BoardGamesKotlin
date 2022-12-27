package me.wtbm.boardgames.ttt

import me.wtbm.boardgames.BoardGames
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

import me.wtbm.boardgames.ttt.TTTGuiController.refreshGUI
import me.wtbm.boardgames.ttt.TTTGuiController.isEndOfGame
import me.wtbm.boardgames.ttt.TTTGuiController.playerInGame
import me.wtbm.boardgames.ttt.TTTGuiController.allowedToMove
import me.wtbm.boardgames.ttt.TTTGuiController.doMove
import me.wtbm.boardgames.ttt.TTTGuiController.refreshGUIForPlayer
import org.bukkit.entity.Player
import java.util.*

class TTTGuiListener(val plugin: BoardGames) : Listener {
    init{ Bukkit.getPluginManager().registerEvents( this, plugin) }

    @EventHandler(priority = EventPriority.NORMAL) // i know, normal is default, but its just there so i dont forget it exist in case i need a lower or higher one
    fun invClick(event: InventoryClickEvent) {
        val p = event.whoClicked as Player
        if(event.view.topInventory == event.clickedInventory) {
            var id: UUID? = playerInGame(p)
            if (id != null) {
                if (allowedToMove(id, p)) {
                    doMove(p, id, event.slot)
                    refreshGUI(id, isEndOfGame(id))
                } else if (isEndOfGame(id) == 0) {
                    refreshGUIForPlayer(id, p)
                }
            }
        }
    }
}
