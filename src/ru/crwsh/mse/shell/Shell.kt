package ru.crwsh.mse.shell

import ru.crwsh.mse.commands.CommandFactory
import ru.crwsh.mse.expansions.ExpansionManager
import ru.crwsh.mse.parser.Parser
import ru.crwsh.mse.parser.tokens.*
import ru.crwsh.mse.utils.EnvLoader
import java.io.*
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future


class Shell(
    private var env: MutableMap<String, String> = mutableMapOf(),
    input_stream: InputStream = System.`in`,
    private val outputStream: OutputStream = System.out,
    private val parser: Parser = Parser(),
    private val commandFactory: CommandFactory = CommandFactory(),
    envLoader : EnvLoader = EnvLoader(),
    private val expansionManager: ExpansionManager = ExpansionManager(env)
) {
    private var inputStream = input_stream

    var inputReader = input_stream.reader()
    val outputWriter = outputStream.writer()
    init {
        if (env.isEmpty())
            env = envLoader.load()
    }

    var publicEnv: Map<String, String> = env
        private set

    fun execute(tokens : MutableList<CrwshToken>) : String {
        expansionManager.performExpansions(tokens)
        return if (tokens.any { it is PipeToken }) {
            executePipeline(tokens)
        }
        else {
             executeCommand(tokens)
        }
    }

    private fun assignVariable(commandTokens : List<CrwshToken>) : String {
        val asTok = commandTokens[0]
        val lhs = asTok.children[0] as WordToken
        val rhs = asTok.children[1] as WordToken
        env[lhs.content] = rhs.content
        return "0"
    }


    private fun executePipeline(pipeline : List<CrwshToken>) : String {
        // pipeline is list of commands split by | symbol
        // technically it is nor command neither operation, it is just a list of commmands processed in specific way
        val cmdList : List<List<CrwshToken>> = parser.getCommandList(pipeline)
        val shells = ShellFactory.getPipedShells(cmdList.size, this, inputStream, outputStream)
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


    private fun executeCommand(commandTokens : List<CrwshToken>) : String {
        return if (commandTokens[0] is AssignmentToken) {
            assignVariable(commandTokens)
        } else if (commandTokens.all { it is WordToken }) {
            val cmd = commandFactory.getByName(commandTokens.map {it as WordToken})
            val res = cmd.execute(env, inputStream, outputStream).toString()
            if (outputStream != System.out) {
                outputStream.close()
            }
            res
        } else {
            outputWriter.write("crwsh: parse error \n")
            "1"
        }
    }

    private fun getTokensFromStdIn() : MutableList<CrwshToken> {
        var line = readLine()!!
        while (!parser.appendLine(line)) {
            if (!parser.canRecover()) {
                outputWriter.write("crwsh: parse error\n")
                return mutableListOf()
            }
            outputWriter.write(parser.getStackString() + ">> ")
            outputWriter.flush()
            line = readLine()!!
        }
        return parser.getTokens()
    }

    fun run() {
        while (true) {
            outputWriter.write("sh >> ")
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