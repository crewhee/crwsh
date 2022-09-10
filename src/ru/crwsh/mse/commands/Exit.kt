package ru.crwsh.mse.commands

import java.io.InputStream
import java.io.OutputStream
import kotlin.system.exitProcess

class Exit(override var args: List<String>) : Command {
    override val name: String
        get() = "command"

    override fun execute(
        env: MutableMap<String, String>, istream: InputStream, ostream: OutputStream
    ): Int {
        val writer = ostream.bufferedWriter()
        if (args.isNotEmpty()) {
            if (args.size == 1) exitProcess(args[0].toIntOrNull() ?: 0)
            else {
                writer.write("exit: too many arguments")
                writer.flush()
                return 1
            }
        }
        exitProcess(env["?"]!!.toInt())
    }
}