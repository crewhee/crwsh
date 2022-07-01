package ru.crwsh.mse.shell

import ru.crwsh.mse.commands.CommandFactory
import ru.crwsh.mse.expansions.ExpansionManager
import ru.crwsh.mse.parser.Parser
import ru.crwsh.mse.parser.tokens.*
import java.io.*
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future


class Shell(
    private var env: MutableMap<String, String> = mutableMapOf(),
    private val input_stream: InputStream = System.`in`,
    private val output_stream: OutputStream = System.out,
    private val parser: Parser = Parser(),
    private val commandFactory: CommandFactory = CommandFactory(),
    private val expansionManager: ExpansionManager = ExpansionManager(env)
) {
    val inputReader = input_stream.reader()
    val outputWriter = output_stream.writer()
    init {
        env["PWD"] = System.getProperty("user.dir")
        env["OLDPWD"] = System.getProperty("user.dir")
        env["PATH"] = "/bin/"
        env["?"] = "0"
    }

    fun execute(tokens : MutableList<CrwshToken>) : String {
        expansionManager.performExpansions(tokens)
        if (tokens.any { it is PipeToken }) {
            return executePipeline(tokens)
        }
        else {
            return executeCommand(tokens)
        }
    }

    fun assignVariable(commandTokens : List<CrwshToken>) : String {
        val asTok = commandTokens[0]
        val lhs = asTok.children[0] as WordToken
        val rhs = asTok.children[1] as WordToken
        env[lhs.content] = rhs.content
        return "0"
    }


    fun executePipeline(pipeline : List<CrwshToken>) : String {
        // pipeline is list of commands split by | symbol
        // technically it is nor command neither operation, it is just a list of commmands processed in specific way
        val cmdList : List<List<CrwshToken>> = parser.getCommandList(pipeline)
        val shells = ShellFactory.getPipedShells(cmdList.size, env, input_stream, output_stream)
        val executor = Executors.newSingleThreadExecutor()
        val futureStack = Stack<Future<String>>()

        for (i in shells.indices){
            futureStack.push(
                executor.submit(Callable {
                    shells[i].executeCommand(cmdList[i])
                    }
                )
            )
        }

        futureStack.forEach { kotlin.runCatching { it.get() }.onSuccess {} }

        return futureStack.peek().get()
    }


    fun executeCommand(commandTokens : List<CrwshToken>) : String {
        if (commandTokens[0] is AssignmentToken) {
            return assignVariable(commandTokens)
        }
        else if (commandTokens.all { it is WordToken }) {
            val cmd = commandFactory.getByName(commandTokens.map {it as WordToken})
            val res = cmd.execute(env, input_stream, output_stream).toString()
            if (output_stream != System.out) {
                output_stream.close()
            }
            return res
        }
        else {
            outputWriter.write("crwsh: parse error \n")
            return "1"
        }
    }

    private fun getTokensFromStdIn() : MutableList<CrwshToken> {
        var line = readLine()!!
        while (!parser.appendLine(line)) {
            if (!parser.canRecover()) {
                outputWriter.write("crwsh: parse error\n")
                return mutableListOf()
            }
            outputWriter.write(parser.getStackString() + ">>")
            outputWriter.flush()
            line = readLine()!!
        }
        return parser.getTokens()
    }

    fun run() {
        while (true) {
            outputWriter.write("sh >>")
            outputWriter.flush()
            val tokens = getTokensFromStdIn()
            if (tokens.isEmpty())
                continue
            env["?"] = execute(tokens)
            parser.reset()
            System.err.flush()
            outputWriter.flush()
        }
    }
}

fun main() {
    val shell = Shell()
    shell.run()
}