package ru.crwsh.mse.expansions

import ru.crwsh.mse.parser.tokens.CmdSubstToken
import ru.crwsh.mse.parser.tokens.CrwshToken
import ru.crwsh.mse.parser.tokens.WordToken

class CommandSubst : Expansion {
    override fun expand(tokens: MutableList<CrwshToken>) {
        TODO()
//        for (i in tokens.indices) {
//            if (tokens[i] is WordToken) {
//                for (child in tokens[i].children) {
//                    if (child is CmdSubstToken) {
//                        // iostream
//                        // shellfactory.getShell(env)
//                        // shell.expand(child.children)
//                        // shell.execute(child.children)
//                        // tokens[i].content += iostream.read()
//                    }
//                }
//                tokens[i].children.removeIf { it is CmdSubstToken }
//            } else {
//                expand(tokens[i].children)
//            }
//        }
    }
}