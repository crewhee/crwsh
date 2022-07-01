package ru.crwsh.mse.commands

import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class Cat(override var args: List<String>) : Command {
    override val name: String
        get() = "cat"

    override fun execute(
        env: MutableMap<String, String>,
        istream: InputStream,
        ostream: OutputStream
    ): Int {
        val result = StringBuilder()
        val options: MutableSet<Char> = hashSetOf()

//        // run without options
//        if (args.size == 1)
//            result.append(readFile(istream.bufferedReader(), options))

        // add options if there are any
        var optionCount = 0
        for (maybeOption in args.drop(1)) {
            if (maybeOption == "-" || maybeOption[0] != '-')
                break
            if (maybeOption.length > 1 && maybeOption[0] == '-') {
                optionCount++
                for (option in maybeOption.drop(1))
                    options.add(option)
            }
        }

        // run if no filenames given
        if (optionCount == args.size - 1)
            result.append(readFile(istream.bufferedReader(), options))

        // run with filenames
        else {
            for (filename in args.drop(optionCount + 1))
                result.append(readFile(File(filename).inputStream().reader().buffered(), options))
        }
        val w = ostream.writer()
        w.write(result.toString())
        w.flush()

        return 0
    }

    private fun readFile(inStream : BufferedReader, options: Set<Char>): StringBuilder {
        val output = StringBuilder()
//        val inStream = if (fileName == "-") System.`in` else File(fileName).inputStream()
        var lineNumber = 0
        var prevLineIsEmpty = false
        for (line in inStream.lines()) {
            if (options.contains('s') && (line.isEmpty() && prevLineIsEmpty))
                continue
            else {
                if (options.contains('b')) {
                    if (line.isNotEmpty()) {
                        lineNumber++
                        output.append("%6d  ".format(lineNumber))
                    }
                } else if (options.contains('n')) {
                    lineNumber++
                    output.append("%6d  ".format(lineNumber))
                }

                output.append(line)

                if (options.contains('e'))
                    output.append("$")
                output.append("\n")
                prevLineIsEmpty = line.isEmpty()
            }
        }
        return output
    }
}