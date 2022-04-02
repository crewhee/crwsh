package ru.crwsh.mse.shell

import ru.crwsh.mse.commands.CommandFactory
import ru.crwsh.mse.parser.Parser
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class Shell(
    private var env: MutableMap<String, String> = mutableMapOf(),
    private val parser: Parser = Parser(),
    private val input_stream: InputStreamReader = InputStreamReader(System.`in`),
    private val output_stream: OutputStreamWriter = OutputStreamWriter(System.out),
    private val commandFactory: CommandFactory = CommandFactory()
) {
    init {
        env["PWD"] = "~/"
        env["PATH"] = "/bin/"
        env["?"] = "0"
    }

    fun run() {
        while (true) {
            output_stream.write("sh >")
            output_stream.flush()
            val command = parser.Parse(env, commandFactory) ?: continue
            env["?"] = command.execute(env, input_stream, output_stream).toString()
            output_stream.write("\n")
            output_stream.flush()
        }
    }
}

fun main() {
    val shell = Shell()
    shell.run()
}