package ru.crwsh.mse.commands

import java.io.File
import java.io.InputStream
import java.io.OutputStream

class Executable(override var args: List<String>) : Command {
    override val name: String
        get() = if (args.isEmpty()) "" else args[0]

    override fun execute(env: MutableMap<String, String>, istream: InputStream, ostream: OutputStream): Int {
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
            ostream.writer().write("crwshell: command not found:  $name")
            ostream.flush()
            return 1
        }

        val process = ProcessBuilder(listOf(binPath) + args.drop(1))
            .directory(File(env["PWD"]?:"/"))
            .redirectInput(ProcessBuilder.Redirect.INHERIT)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .start()
        process.waitFor()
        return process.exitValue()
    }
}