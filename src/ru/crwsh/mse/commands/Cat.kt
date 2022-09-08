package ru.crwsh.mse.commands

import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.OutputStream


class Cat(override var args: List<String>) : Command {
    override val name: String
        get() = "cat"

    private fun readFile(inStream: BufferedReader, options: Set<String>): StringBuilder {
        val output = StringBuilder()
        var lineNumber = 0
        var prevLineIsEmpty = false
        for (line in inStream.lines()) {
            if (options.contains("s") && (line.isEmpty() && prevLineIsEmpty))
                continue
            else {
                if (options.contains("b")) {
                    if (line.isNotEmpty()) {
                        lineNumber++
                        output.append("%6d  ".format(lineNumber))
                    }
                } else if (options.contains("n")) {
                    lineNumber++
                    output.append("%6d  ".format(lineNumber))
                }

                output.append(line)

                if (options.contains("e"))
                    output.append("$")
                output.append("\n")
                prevLineIsEmpty = line.isEmpty()
            }
        }
        return output
    }

    override fun execute(
        env: MutableMap<String, String>,
        istream: InputStream,
        ostream: OutputStream
    ): Int {
        val result = StringBuilder()
        val options: Set<String> = flagParser()

        // run if no filenames given
        if (options.size == args.size - 1)
            result.append(readFile(istream.bufferedReader(), options))

        // run with filenames
        else {
            for (filename in args.drop(options.size + 1))
                try {
                    result.append(
                        readFile(
                            File(env["PWD"], filename).inputStream().reader().buffered(),
                            options
                        )
                    )
                } catch (e : FileNotFoundException) {
                    result.append("cat: $filename: No such file or directory\n")
                }
        }

        val w = ostream.writer()
        w.write(result.toString())
        w.flush()

        return 0
    }
}