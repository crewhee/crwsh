package ru.crwsh.mse.commands

import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import kotlin.streams.toList

// -i case insensetive
// -A n (collect n lines after match)
// -v find full word

class Grep(override var args: List<String>) : Command {
    override val name: String
        get() = "grep"

    private fun readInput(pattern: String, istream: BufferedReader): List<String> =
        istream.lines().filter { it.contains(pattern) }.toList()

    override fun execute(env: MutableMap<String, String>, istream: InputStream, ostream: OutputStream): Int {
//        val flags = flagParser()
        val outputStream = ostream.bufferedWriter()
        val res: MutableList<String> = mutableListOf()

        var pattern: String? = null
        val location: MutableList<String> = mutableListOf()
        for (arg in args) {
            if (arg[0] == '-') continue
            if (pattern == null) pattern = arg
            else location.add(arg)
        }
        if (pattern == null) {
            outputStream.write("bad arguments \n")
            outputStream.flush()
            return 1
        }

        if (location.isEmpty()) res.addAll(readInput(pattern, istream.bufferedReader()))
        else {
            for (filename in location) {
                val file = File(filename)
                if (file.isFile) readInput(pattern, file.bufferedReader()).forEach { outputStream.write(it + "\n") }
                else outputStream.write("grep: $filename: No such file or directory")
                outputStream.flush()
            }
        }

        return 0
    }
}