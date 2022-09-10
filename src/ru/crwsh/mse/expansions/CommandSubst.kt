package ru.crwsh.mse.expansions

import ru.crwsh.mse.parser.tokens.CmdSubstToken
import ru.crwsh.mse.parser.tokens.CrwshToken
import ru.crwsh.mse.parser.tokens.WordToken
import ru.crwsh.mse.shell.ShellFactory
import java.io.PipedInputStream
import java.io.PipedOutputStream

class CommandSubst(val env: MutableMap<String, String>) : Expansion {

    override fun expand(tokens: MutableList<CrwshToken>) {
        for (i in tokens.indices) {
            if (tokens[i] is WordToken) {
                for (child in tokens[i].children) {
                    if (child is CmdSubstToken) {
                        val istream = PipedInputStream()
                        val ostream = PipedOutputStream()
                        ostream.connect(istream)
                        val shell = ShellFactory.getSingleShell(env, ostream)
                        shell.execute(child.children)
                        (tokens[i] as WordToken).content += istream.bufferedReader().readLine()
                    }
                }
                tokens[i].children.removeIf { it is CmdSubstToken }
            } else {
                expand(tokens[i].children)
            }
        }
    }
}