package ru.crwsh.mse.commands

import java.io.InputStreamReader
import java.io.OutputStreamWriter

class Pwd(override var args: List<String>) : Command {
    override val name: String
        get() = "pwd"

    override fun execute(
        env: Map<String, String>,
        istream: InputStreamReader,
        ostream: OutputStreamWriter
    ): Int {
        var flag_p = true
        var flag_l = false
        for (arg in args) {
            if (arg == "-P") {
                flag_p = true
            }
            if (arg == "-L") {
                flag_l = true
            } else {
                System.err.println("pwd: bad option: $arg")
                return 1
            }
        }

        ostream.write(env["PWD"] ?: return 1)

        return 0
    }
}