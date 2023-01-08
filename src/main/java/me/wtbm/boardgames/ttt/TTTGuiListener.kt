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

object TTTGuiListener : Listener {

    @EventHandler(priority = EventPriority.NORMAL) // I know, normal is default, but It's just there, so I don't forget it exists in case I need a lower or higher one
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
