package me.wtbm.boardgames.ttt

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*

 object TTTGuiController : TTTController {

    override fun startGame(r1: Player, r2: Player):UUID {
        val msg = TextComponent("If you close the game before it ended you  can §2click this to open again")
        msg.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tttg open")
        r1.spigot().sendMessage(msg)
        r2.spigot().sendMessage(msg)

        val id = super.startGame(r1, r2)
        //val game = plugin.runningGames[id]!!
        refreshGUI(id, 0)
        return id;
    }

     private fun getItemStack( number: Int, turn: Boolean) : ItemStack{
         var item: ItemStack;
        if(number == 1) {
            item = ItemStack(Material.RED_STAINED_GLASS_PANE, 1)
            var meta = (item.itemMeta)
            meta?.setDisplayName("§l§4X")
            item.itemMeta = meta
        }
        else if(number == 2) {
            item =  ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1)
            var meta = (item.itemMeta)
            meta?.setDisplayName("§l§1O")
            item.itemMeta = meta
        }
         else if(!turn) {
             item =  ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1)
            var meta = (item.itemMeta)
            meta?.setDisplayName("§7-")
            item.itemMeta = meta
        }
         else {
            item = ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1)
            var meta = (item.itemMeta)
            meta?.setDisplayName("§f§lPLACE")
            item.itemMeta = meta
        }
         return item;
     }

     fun allowedToMove(id: UUID, pl: Player) : Boolean{
         val game = plugin.runningGames[id]
         if (game != null)
             return game.isPlayersTurn(pl)
         return false;
     }

     fun refreshGUIForPlayer( id: UUID,  p: Player) {
         val game = plugin.runningGames[id] ?: return
         refreshGUIForPlayer(id, isEndOfGame(id), p, if( (if (game.pl1turn) game.p1 else game.p2) == p) game.pl1turn else !game.pl1turn )
     }

    fun refreshGUIForPlayer( id: UUID, win: Int, p: Player, turn: Boolean) {
        val game = plugin.runningGames[id]
        val field = game!!.field
        val othername = if (game.p1 == p) game.p2.name else game.p1.name
        val title = if(win == 0)( if(turn)"your turn" else "${othername}'s turn" )
        else if (win == 1) "${game.p1.name} won!!"
        else if (win == 2) "${game.p2.name} won!!"
        else "its a tie :("
        var inv = Bukkit.createInventory(null, InventoryType.DISPENSER, title)

        for (i in 0..8) {
            if(win == 0 && turn) inv.setItem(i, getItemStack(field[i], true))
            else inv.setItem(i, getItemStack(field[i], false))
        }
        p.openInventory(inv)

    }

    fun refreshGUI(id: UUID, win: Int) {
        val game = plugin.runningGames[id]
        val field = game!!.field
        val tp = if (game.pl1turn) game.p1 else game.p2 //turning player
        val wp = if (game.pl1turn) game.p2 else game.p1 //waiting player

        refreshGUIForPlayer(id, win , wp, false)
        refreshGUIForPlayer(id, win , tp, true)
        // val turn = if(game.p1 == p) game.pl1turn else !game.pl1turn
        //val turn = if( (if (game.pl1turn) game.p1 else game.p2) == p) game.pl1turn else !game.pl1turn //thisone if you want to be able to play agains yourself
        if(win != 0) endGame(id)
    }


}