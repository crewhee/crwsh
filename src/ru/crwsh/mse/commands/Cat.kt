package ru.crwsh.mse.commands

import java.io.File

class Cat() : Command {
    override val type: String
        get() = "command"

    override fun Execute(args: List<String>, env: Map<String, String>): String {
        val result = StringBuilder()
        val options : MutableSet<Char> = hashSetOf()
        // run without options
        if (args.isEmpty())
            result.append(readFile("-", options))

        // add options if there are any
        var optionCount = 0
        for (maybeOption in args) {
            if (maybeOption == "-" || maybeOption[0] != '-')
                break
            if (maybeOption.length > 1 && maybeOption[0] == '-') {
                optionCount++
                for (option in maybeOption.drop(1))
                    options.add(option)
            }
        }

        // run if no filenames given
        if (optionCount == args.size)
            result.append(readFile("-", options))

        // run with filenames
        for (fileName in args.drop(optionCount))
            result.append(readFile(fileName, options))

        return result.toString()
    }

    private fun readFile(fileName: String, options: Set<Char>): StringBuilder {
        val output = StringBuilder()
        val inStream = if (fileName == "-") System.`in` else File(fileName).inputStream()
        var lineNumber = 0
        var prevLineIsEmpty = false
        for (line in inStream.bufferedReader().lines()) {
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