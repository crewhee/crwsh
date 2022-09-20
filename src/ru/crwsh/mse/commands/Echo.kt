package ru.crwsh.mse.commands

import java.io.InputStream
import java.io.OutputStream

class Echo(override var args: List<String>) : Command {
    override val name: String
        get() = "echo"

    override fun execute(
        env: MutableMap<String, String>,
        istream: InputStream,
        ostream: OutputStream
    ): Int {
        val result = StringBuilder()
        for (arg in args.drop(1))
            result.append("$arg ")
        result.append("\n")
        val w = ostream.writer()
        w.write(result.toString())
        w.flush()
        return 0
    }
}
