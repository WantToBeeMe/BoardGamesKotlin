package me.wtbm.boardgames.carcassone

import me.wtbm.boardgames.BoardGames
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import java.io.File
import java.util.*

object CarcassonneFileController {
    private val plugin get() = BoardGames.instance
    val dataFolder = plugin.dataFolder;
    fun generate(locBig: Location, locSmall: Location, name: String,  offX:Int, offZ: Int, generateCarcas: Boolean): Boolean {
        generateFile(name)
        generateBlockList(locBig, locSmall, name, offX, offZ, generateCarcas)
        return true;

    }
    private fun generateFile(name:String) {
        Bukkit.getLogger().info(dataFolder.toString())
        val file = File(dataFolder, "$name.txt")
        if(!dataFolder.exists()) dataFolder.mkdir();
        if (!file.exists()) {
            file.createNewFile()
            Bukkit.getLogger().info("$name is created successfully.")
        } else Bukkit.getLogger().info("$name already exists.")
    }

    fun getAllFileNames() : List<String>{
        val list : MutableList<String> = mutableListOf<String>();
        dataFolder.walk().forEach {
            if( it.isFile) list.add(it.name.removeSuffix(".txt"))
        }
        return list;
    }

    fun placeStructureFile(name : String, loc : Location, rotation : Int) : Boolean{
        val file = File(dataFolder, "$name.txt")
        if(!file.exists())return false
        var blockIdString : List<String> =  mutableListOf<String>()
        val field : MutableList<String> = mutableListOf<String>()
        var fieldCheck = false
        var offsetts : List<String> =  mutableListOf<String>()
        //read the file
        file.readLines().forEach(){
            if(it.startsWith("blocks::")){
                blockIdString = it.removePrefix("blocks::").split(";")
            }
            if(it.startsWith("offset::")){
                offsetts = it.removePrefix("offset::").split(";")
            }
            if(fieldCheck) field.add(it)
            if(it == "#Field") fieldCheck = true
        }

        //read the block and ids
        var blockIds: MutableMap<String, Material> = HashMap()
        blockIdString.forEach(){
            val splitter : List<String> = it.split(":")
            if(splitter.size == 2)
                blockIds[splitter[0]] = Material.valueOf(splitter[1])
        }
        //placing
        var offsetZ = offsetts[0].toInt()
        var offsetX = offsetts[1].toInt()

        var addX = if(rotation < 180) -offsetX else offsetX
        var addY = 0
        var addZ = if(rotation < 90 || rotation >= 270) -offsetZ else offsetZ

        val world = loc.world ?: return false;
        field.forEach(){lines ->
            lines.split("y").forEach(){chunks ->
                chunks.split("z").forEach(){parts ->
                    blockIds.get(parts)
                        ?.let {
                            world.getBlockAt(loc.blockX + addX, loc.blockY + addY, loc.blockZ + addZ).setType(it)
                        }
                    when (rotation) {
                        0 -> addZ++
                        90 -> addX++
                        180 -> addZ--
                        270 -> addX--
                    }
                }
                when (rotation) {
                    0 -> addZ = -offsetZ
                    90 -> addX = -offsetX
                    180 -> addZ = offsetZ
                    270 -> addX = offsetX
                }
                addY++
            }
            when (rotation) {
                0 -> addZ = -offsetZ
                90 -> addX = -offsetX
                180 -> addZ = offsetZ
                270 -> addX = offsetX
            }
            addY = 0
            when (rotation) {
                0 -> addX++
                90 -> addZ--
                180 -> addX--
                270 -> addZ++
            }
        }
        return true;
    }

    private fun generateBlockList(locBig: Location, locSmall: Location, name: String, offX:Int, offZ: Int , generateCarcas: Boolean) {
        var file = File(dataFolder, "$name.txt")
        var setup = "#Setup\n";
        var field = "\n\n#Field\n";
        var carcas = "\ncarcass::"
        var blockIds: MutableMap<Material, Int> = HashMap()
        var index = 0
        var onY0: Boolean;
        for (x in locSmall.blockX..locBig.blockX) {
            onY0 = true;
            for (y in locSmall.blockY..locBig.blockY) {
                for (z in locSmall.blockZ..locBig.blockZ) {
                    var mat = locSmall.world?.getBlockAt(x,y,z)?.type
                    if(mat == Material.AIR || mat == null) field += "-z"
                    else {
                        if(!blockIds.containsKey(mat)) blockIds.put(mat, index++)
                        field += "${blockIds[mat]}z"
                    }
                }
                field += "y"
                onY0 = false;
            }
            field += "\n"
        }
        setup += "blocks::"
        for (blockId in blockIds) {
            setup += "${blockId.value}:${blockId.key};"
        }
        setup +="\noffset::${offX};${offZ}"
        file.writeText(setup)
        if(generateCarcas) file.appendText(carcas)
        file.appendText(field)
    }


}
