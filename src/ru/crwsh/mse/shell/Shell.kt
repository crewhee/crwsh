package ru.crwsh.mse.shell

import ru.crwsh.mse.commands.Command
import ru.crwsh.mse.commands.CommandFactory
import ru.crwsh.mse.commands.CommandType
import ru.csh.mse.commands.*
import java.io.PrintStream
import java.util.Scanner
import ru.crwsh.mse.parser.Parser

class Shell (
    private var env : MutableMap<String, String> = mutableMapOf(),
    private val parser : Parser = Parser(),
    private val input_stream : Scanner = Scanner(java.lang.System.`in`),
    private val output_stream : PrintStream = PrintStream(System.out),
    private var commandGetter : MutableMap<String, Command> = mutableMapOf(),
    commandFactory: CommandFactory = CommandFactory()
)  {
    init {
        env["PWD"] = "~/"
        for (k in CommandType.values())
            commandGetter[commandFactory.getName(k)] = commandFactory.makeCommand(k)
    }

    fun run() {
        while (true) {
            var p_res = parser.Parse(input_stream, env)
            var res : String?
            if (p_res.commandName in commandGetter) {
                res = commandGetter[p_res.commandName]?.Execute(p_res.args, env)

                output_stream.println(res)
            }
            else {
                output_stream.println("csh: command not found: " + p_res.commandName)
            }
            output_stream.println("sh >")
        }
    }
}

fun main() {
    val shell = Shell()
    shell.run()
}