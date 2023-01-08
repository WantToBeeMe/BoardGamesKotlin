package me.wtbm.boardgames.carcassone

import me.wtbm.boardgames.BoardGames
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.ChatColor
import org.bukkit.Location
import java.util.*

import me.wtbm.boardgames.carcassone.CarcassonneFileController.generate
import me.wtbm.boardgames.carcassone.CarcassonneFileController.getAllFileNames
import me.wtbm.boardgames.carcassone.CarcassonneFileController.placeStructureFile
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.TabCompleter

object CarcassonneCommand : CommandExecutor{
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

        if (args[0] == "generate") return generate(sender, Arrays.copyOfRange(args, 1, args.size))
        if (args[0] == "get") return get(sender)
        if (args[0] == "place") return place(sender, Arrays.copyOfRange(args, 1, args.size))

        return false
    }
    private fun place(p: Player, args: Array<out String>) : Boolean{

        if(args.isEmpty()){
            p.sendMessage("Specify what to place")
            return true
        }
        var rotation = 0
        if(args.size == 2){
            rotation = args[1].toInt()
        }
       return placeStructureFile(args[0], p.location, rotation)
    }


    private fun get(p: Player) : Boolean{
        var list = getAllFileNames()
        list.forEach(){
            p.sendMessage(it)
        }
        if(list.isEmpty()) p.sendMessage("There are no files")
        return true;
    }

    private fun generate(p: Player, args: Array<String>): Boolean {
        //still need to make a check if the players arent already in a game
        if (args.size < 7) {
                p.sendMessage("${ChatColor.DARK_RED}you didn't put in the right coordinates or/and name")
                return true
            }
        return try{
            val biggestArg: (String, String) -> Double = { arg1, arg2 -> if (arg1.toDouble() > arg2.toDouble()) arg1.toDouble() else arg2.toDouble()}
            val smallestArg: (String, String) -> Double = { arg1, arg2 -> if (arg1.toDouble() <= arg2.toDouble()) arg1.toDouble() else arg2.toDouble()}
            val locBig = Location(p.world, biggestArg(args[0], args[3]) , biggestArg(args[1], args[4]), biggestArg(args[2], args[5]) )
            val locSmall = Location(p.world, smallestArg(args[0], args[3]) , smallestArg(args[1], args[4]), smallestArg(args[2], args[5]) )
            var offX = 0
            var offZ = 0
            var generateCarcas = false;
            if(args.size == 8){
                if (args[7] == "carcas"){
                    generateCarcas = true
                    offZ = 4
                    offX = 4
                }
            }
            if(args.size == 9){
                offX = args[7].toInt()
                offZ = args[8].toInt()
            }
            generate(locBig, locSmall, args[6], offX, offZ, generateCarcas);
        } catch (e : Exception){
            p.sendMessage("${ChatColor.DARK_RED}you didn't put in coordinates of the right type / didn't put in a possible name")
            true
        }
    }
}

object CarcassonneTabCompleter : TabCompleter {

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): List<String> {
        var list : MutableList<String> = mutableListOf<String>();

        if (sender !is Player) return list
        val p : Player = sender
        if (args.isEmpty() || args[0] == "") {
            list.add("generate")
            list.add("get")
            list.add("place")
            return list
        }
        if (args[0] == "generate") {
            val loc = p.getTargetBlock(setOf(Material.AIR),6).location
            if(args.size == 2 || args.size == 5)
                list.add("${loc.x.toInt()} ${loc.y.toInt()} ${loc.z.toInt()}")
            else if(args.size == 3 || args.size == 6)
                list.add("${loc.y.toInt()} ${loc.z.toInt()}")
            else if(args.size == 4 || args.size == 7)
                list.add("${loc.z.toInt()}")
            else if(args.size == 8)
                list.add("name")
            return list
        }
        if(args[0] == "place"){
            if(args.size == 2){
                return getAllFileNames()
            }else{
                list.add("90")
                list.add("180")
                list.add("270")
                return list
            }
        }

        return list;
    }
}