package ru.crwsh.mse.commands

import java.io.InputStream
import java.io.OutputStream

class Pwd(override var args: List<String>) : Command {
    override val name: String
        get() = "pwd"

    override fun execute(
        env: MutableMap<String, String>, istream: InputStream, ostream: OutputStream
    ): Int {
        val w = ostream.writer()
        if (env["PWD"] == null) return 1
        w.write(env["PWD"] + "\n")
        w.flush()
        return 0
    }
}