package ru.crwsh.mse.commands

import java.io.InputStream
import java.io.OutputStream

interface Command {
    val name: String
    var args: List<String>

    fun execute(
        env: MutableMap<String, String>, istream: InputStream = System.`in`, ostream: OutputStream = System.out
    ): Int

    fun flagParser(): Set<String> {
        val res = mutableSetOf<String>()
        for (arg in args.drop(1)) {
            if (arg == "-") break
            if (arg[0] == '-' && arg.length > 1) if (arg[1] == '-') res.add(arg.drop(2))
            else for (f in arg.drop(1)) res.add(f.toString())
        }
        return res
    }
}