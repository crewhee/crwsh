package ru.crwsh.mse.commands

import java.io.InputStreamReader
import java.io.OutputStreamWriter

class Echo(override var args: List<String>) : Command {
    override val name: String
        get() = "echo"

    override fun execute(
        env: Map<String, String>,
        istream: InputStreamReader,
        ostream: OutputStreamWriter
    ): Int {
        val result = StringBuilder()
        for (arg in args)
            result.append("$arg ")
        ostream.write(result.toString())
        return 0
    }
}
