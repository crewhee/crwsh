package ru.crwsh.mse.commands

import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import kotlin.streams.toList

// -i case-insensitive
// -A n (collect n lines after match)

class Grep(override var args: List<String>) : Command {
    private val r = GrepReader()
    private val INVALID_ARGUMENT = "grep: Invalid argument"

    private class GrepReader() {
        enum class Strategy {DEFAULT, N_LINES_AFTER}

        var s: Strategy = Strategy.DEFAULT

        fun setStrategy(s: Strategy) {
            this.s = s
        }

        fun execute(pattern: Regex, istream: BufferedReader, n : Int? = 0) : List<String> {
            return when(s) {
                Strategy.DEFAULT -> readInput(pattern, istream)
                Strategy.N_LINES_AFTER -> readInputNLines(pattern, istream, n)
            }
        }

        private fun readInput(pattern: Regex, istream: BufferedReader): List<String> =
            istream.lines().filter { pattern.containsMatchIn(it) }.toList()

        private fun readInputNLines(pattern: Regex, istream: BufferedReader, n : Int?) : List<String> {
            if (n == null) {
                return listOf()
            }
            var counter = 0
            val res = mutableListOf<String>()
            istream.lines()
                .forEach {
                    if (pattern.containsMatchIn(it)) {
                        counter = 0
                    }
                    if (counter < n + 1) {
                        res.add(it)
                        counter++
                    }
                }
            return res
        }
    }

    override val name: String
        get() = "grep"

    override fun execute(env: MutableMap<String, String>, istream: InputStream, ostream: OutputStream): Int {
        val flags = flagParser()
        var linesCounter : Int? = null
        val outputStream = ostream.bufferedWriter()
        if (flags.contains("A")) {
            r.setStrategy(GrepReader.Strategy.N_LINES_AFTER)
        }

        var pattern: String? = null
        val location: MutableList<String> = mutableListOf()
        for (arg in args.drop(1)) {
            if (arg[0] == '-') continue
            else if (linesCounter == null && flags.contains("A")) {
                try {
                    linesCounter = arg.toInt()
                } catch (e: NumberFormatException) {
                    outputStream.write(INVALID_ARGUMENT)
                    outputStream.flush()
                    return 2
                }
            }
            else if (pattern == null) pattern = arg
            else location.add(arg)
        }

        if (pattern == null) {
            outputStream.write(INVALID_ARGUMENT)
            outputStream.flush()
            return 1
        }

        val regex = Regex(pattern,
            when {
                flags.contains("i") -> setOf(RegexOption.IGNORE_CASE)
                else -> setOf()
            }
        )

        if (location.isEmpty()) {
            r.execute(regex, istream.bufferedReader(), linesCounter)
                .forEach { outputStream.write(it) }
            outputStream.flush()
        }
        else {
            for (filename in location) {
                val file = File(filename)
                if (file.isFile) r.execute(regex, file.bufferedReader(), linesCounter).forEach { outputStream.write(it + "\n") }
                else outputStream.write("grep: $filename: No such file or directory")
                outputStream.flush()
            }
        }

        return 0
    }
}