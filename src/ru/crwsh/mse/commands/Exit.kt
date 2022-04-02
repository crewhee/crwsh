package ru.crwsh.mse.commands

import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.system.exitProcess

class Exit(override var args: List<String>) : Command {
    override val name: String
        get() = "command"

    @Suppress("UNREACHABLE_CODE")
    override fun execute(
        env: Map<String, String>,
        istream: InputStreamReader,
        ostream: OutputStreamWriter
    ): Int {
        if (args.size > 0) {
            if (args.size == 1)
                return exitProcess(args[0].toIntOrNull() ?: 0)
            else {
                System.err.println("exit: too many arguments")
                return 1
            }
        }
        return exitProcess(env["?"]!!.toInt())
    }
}