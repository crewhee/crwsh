package ru.crwsh.mse.commands

import java.io.InputStreamReader
import java.io.OutputStreamWriter

class Wc(override var args: List<String>) : Command {
    override val name: String
        get() = "command"

    override fun execute(
        env: Map<String, String>,
        istream: InputStreamReader,
        ostream: OutputStreamWriter
    ): Int {
        TODO("Not yet implemented")
    }

}
