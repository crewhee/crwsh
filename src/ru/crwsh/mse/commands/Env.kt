package ru.crwsh.mse.commands

import java.io.InputStream
import java.io.OutputStream

class Env(override var args: List<String>) : Command {
    override val name: String
        get() = "env"

    override fun execute(env: MutableMap<String, String>, istream: InputStream, ostream: OutputStream): Int {
        val w = ostream.writer()
        for (v in env.entries)
            w.write(v.toString() + "\n")
        w.flush()
        ostream.flush()
        return 0
    }
}