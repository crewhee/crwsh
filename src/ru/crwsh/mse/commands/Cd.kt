package ru.crwsh.mse.commands

import java.io.File
import java.io.InputStream
import java.io.OutputStream

class Cd(override var args: List<String>) : Command {
    override val name: String
        get() = "cd"

    override fun execute(env: MutableMap<String, String>, istream: InputStream, ostream: OutputStream): Int {
        val f = File(args[args.size - 1])
        env["PWD"] = f.toString()
        return 0
    }

}
