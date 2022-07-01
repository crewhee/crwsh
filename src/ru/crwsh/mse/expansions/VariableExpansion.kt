package ru.crwsh.mse.expansions

import ru.crwsh.mse.parser.tokens.AssignmentToken
import ru.crwsh.mse.parser.tokens.CrwshToken
import ru.crwsh.mse.parser.tokens.VariableToken
import ru.crwsh.mse.parser.tokens.WordToken

class VariableExpansion(val env : MutableMap<String, String>) : Expansion {
    override fun expand(tokens: MutableList<CrwshToken>) {
        for (i in tokens.indices) {
            if (tokens[i] is WordToken) {
                for (child in tokens[i].children) {
                    if (child is VariableToken) {
                        val name = (child.children[0] as WordToken).content
                        (tokens[i] as WordToken).content += env[name] ?: ""
                    }
                }
                tokens[i].children.removeIf { it is VariableToken }
            } else {
                expand(tokens[i].children)
            }
        }
    }
}