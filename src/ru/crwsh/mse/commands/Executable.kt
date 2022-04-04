package ru.crwsh.mse.commands

import java.io.File
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class Executable(override var args: List<String>) : Command {
    override val name: String
        get() = if (args.isEmpty()) "" else args[0]

    override fun execute(env: Map<String, String>, istream: InputStreamReader, ostream: OutputStreamWriter): Int {
        if (name.isEmpty())
            return env["?"]!!.toInt()
        var binPath: String? = null
        if (File(name).exists())
            binPath = name
        else {
            if (env["PATH"] == null) {
                System.err.println("Empty path")
                return 1
            }
            for (path in env["PATH"]!!.split(':')) {
                for (it in File(path).walk()) {
                    if (it.name == name) {
                        binPath = path + it.name
                        break
                    }
                }
                if (binPath != null)
                    break
            }
        }

        if (binPath == null) {
            System.err.println("crwshell: command not found:  $name")
            return 1
        }

        val process = ProcessBuilder(listOf(binPath) + args.drop(1))
            .redirectInput(ProcessBuilder.Redirect.INHERIT)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .start()
        process.waitFor()
        return process.exitValue()
    }
}